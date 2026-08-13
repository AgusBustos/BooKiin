const API_URL = () => {
    let url = document.getElementById('api-url').value;
    if (!url) {
        url = `http://${window.location.hostname}:8080/api`;
        document.getElementById('api-url').value = url;
    }
    return url;
};
let html5QrcodeScanner = null;

// Navigation
function navTo(targetView) {
    // Stop scanner if leaving scan view
    if (targetView !== 'scan' && html5QrcodeScanner) {
        html5QrcodeScanner.clear().catch(error => console.error("Failed to clear scanner", error));
        html5QrcodeScanner = null;
    }

    // Update Nav Icons
    document.querySelectorAll('.nav-item').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.target === targetView);
    });

    // Update Views
    document.querySelectorAll('.view').forEach(view => {
        view.classList.toggle('active', view.id === `view-${targetView}`);
    });

    // Initialize specifics for views
    if (targetView === 'scan') {
        initScanner();
    } else if (targetView === 'home') {
        loadDashboardStats();
    } else if (targetView === 'inventory') {
        loadInventory();
    }
}

// Manual Entry
function openManualEntry() {
    navTo('scan');
    // If scanner is running, clear it since we are doing manual
    if (html5QrcodeScanner) {
        html5QrcodeScanner.clear().catch(e => console.error(e));
        html5QrcodeScanner = null;
    }
    document.getElementById('scan-result').classList.remove('hidden');
    document.getElementById('book-form').reset();
    document.getElementById('book-isbn').readOnly = false; // Allow typing ISBN manually
    document.getElementById('book-isbn').focus();
}

// Scanner Logic
function initScanner() {
    document.getElementById('scan-result').classList.add('hidden');
    html5QrcodeScanner = new Html5QrcodeScanner(
        "reader",
        { fps: 10, qrbox: {width: 250, height: 150} },
        /* verbose= */ false
    );
    html5QrcodeScanner.render(onScanSuccess, onScanFailure);
}

function onScanSuccess(decodedText, decodedResult) {
    html5QrcodeScanner.clear();
    if('vibrate' in navigator) navigator.vibrate(100);
    fetchBookInfo(decodedText);
}

async function fetchBookInfo(isbn) {
    showLoader(true);
    const resultDiv = document.getElementById('scan-result');
    const isbnInput = document.getElementById('book-isbn');
    const titleInput = document.getElementById('book-title');
    const authorInput = document.getElementById('book-author');
    const publisherInput = document.getElementById('book-publisher');
    const categoryInput = document.getElementById('book-category');
    const shelfInput = document.getElementById('book-shelf');
    const urlInput = document.getElementById('book-url');
    
    isbnInput.value = isbn;
    isbnInput.readOnly = true;
    
    try {
        const response = await fetch(`${API_URL()}/libros/externo/${isbn}`);
        if (response.ok) {
            const data = await response.json();
            titleInput.value = data.titulo || '';
            authorInput.value = data.autor || '';
            publisherInput.value = data.editorial || '';
            categoryInput.value = data.categoria || '';
            shelfInput.value = data.estanteria || '';
            urlInput.value = data.urlPortada || '';
        } else {
            // Not found, clear fields for manual entry
            titleInput.value = '';
            authorInput.value = '';
            publisherInput.value = '';
            categoryInput.value = '';
            shelfInput.value = '';
            urlInput.value = '';
            alert('Libro no encontrado en Google Books. Puedes ingresar los datos manualmente.');
        }
        resultDiv.classList.remove('hidden');
    } catch (error) {
        console.error('Error fetching book info:', error);
        alert('Error conectando con el servidor.');
    } finally {
        showLoader(false);
    }
}

document.getElementById('book-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    showLoader(true);
    
    const libro = {
        isbn: document.getElementById('book-isbn').value,
        titulo: document.getElementById('book-title').value,
        autor: document.getElementById('book-author').value,
        editorial: document.getElementById('book-publisher').value,
        categoria: document.getElementById('book-category').value,
        estanteria: document.getElementById('book-shelf').value,
        urlPortada: document.getElementById('book-url').value
    };

    try {
        const response = await fetch(`${API_URL()}/libros`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(libro)
        });
        
        if (response.ok) {
            alert('Libro guardado con éxito!');
            document.getElementById('book-form').reset();
            document.getElementById('scan-result').classList.add('hidden');
            navTo('home');
        } else {
            alert('Error al guardar el libro.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error de conexión con el servidor.');
    } finally {
        showLoader(false);
    }
});

function onScanFailure(error) {
    // Handle scan failure silently
}

// API Calls
function showLoader(show) {
    document.getElementById('loader').classList.toggle('hidden', !show);
}



// Loan Form Handling
document.getElementById('loan-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const dni = document.getElementById('loan-dni').value;
    const idEjemplar = document.getElementById('loan-ejemplar').value;
    const msgDiv = document.getElementById('loan-msg');
    
    showLoader(true);
    try {
        const params = new URLSearchParams({
            dniSocio: dni,
            idEjemplar: idEjemplar
        });
        
        const response = await fetch(`${API_URL()}/prestamos?${params.toString()}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            msgDiv.className = 'msg success';
            msgDiv.innerText = 'Préstamo registrado con éxito';
            msgDiv.classList.remove('hidden');
            document.getElementById('loan-form').reset();
            loadDashboardStats();
        } else {
            msgDiv.className = 'msg error';
            msgDiv.innerText = 'Error al registrar el préstamo';
            msgDiv.classList.remove('hidden');
        }
    } catch (error) {
        msgDiv.className = 'msg error';
        msgDiv.innerText = 'Error de conexión';
        msgDiv.classList.remove('hidden');
    } finally {
        showLoader(false);
        setTimeout(() => msgDiv.classList.add('hidden'), 3000);
    }
});

// Load Inventory (CRUD List)
async function loadInventory() {
    showLoader(true);
    const listDiv = document.getElementById('inventory-list');
    listDiv.innerHTML = '';
    
    try {
        const response = await fetch(`${API_URL()}/libros`);
        if (response.ok) {
            const libros = await response.json();
            if (libros.length === 0) {
                listDiv.innerHTML = '<p>No hay libros registrados aún.</p>';
                return;
            }
            
            libros.forEach(libro => {
                const card = document.createElement('div');
                card.style = 'background: white; padding: 1rem; border-radius: 0.75rem; display: flex; gap: 1rem; align-items: center; border: 1px solid #e2e8f0;';
                
                const img = libro.urlPortada 
                    ? `<img src="${libro.urlPortada}" style="width: 50px; height: 75px; object-fit: cover; border-radius: 4px;" onerror="this.src='data:image/svg+xml;utf8,<svg xmlns=\\'http://www.w3.org/2000/svg\\' width=\\'50\\' height=\\'75\\'><rect width=\\'50\\' height=\\'75\\' fill=\\'%23e2e8f0\\'/></svg>'">`
                    : `<div style="width: 50px; height: 75px; background: #e2e8f0; border-radius: 4px; display:flex; align-items:center; justify-content:center;"><i class="ph ph-book" style="color:#94a3b8"></i></div>`;
                
                card.innerHTML = `
                    ${img}
                    <div style="flex: 1; overflow: hidden;">
                        <h4 style="margin:0; font-size: 1rem; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${libro.titulo || 'Sin título'}</h4>
                        <p style="margin:0; font-size: 0.8rem; color: #64748b;">${libro.autor || 'Autor desconocido'}</p>
                        <p style="margin:0; font-size: 0.75rem; color: #94a3b8; font-family: monospace;">ISBN: ${libro.isbn} | Estantería: <strong style="color:#0f172a">${libro.estanteria || 'N/A'}</strong></p>
                    </div>
                    <button class="primary-btn" style="width:auto; padding: 0.5rem 1rem;" onclick="editBook('${libro.isbn}')">Editar</button>
                `;
                listDiv.appendChild(card);
            });
        }
    } catch (error) {
        console.error("Error cargando inventario", error);
        listDiv.innerHTML = '<p style="color:red">Error cargando inventario.</p>';
    } finally {
        showLoader(false);
    }
}

// Edit Book from Inventory
function editBook(isbn) {
    navTo('scan');
    // Clear scanner if running
    if (html5QrcodeScanner) {
        html5QrcodeScanner.clear().catch(e => console.error(e));
        html5QrcodeScanner = null;
    }
    // Set field readonly
    document.getElementById('book-isbn').readOnly = true;
    // Fetch data locally from API to populate form
    fetchBookInfoFromApi(isbn);
}

// Helper to fetch exactly from our backend, not Google/OpenLibrary
async function fetchBookInfoFromApi(isbn) {
    showLoader(true);
    try {
        const response = await fetch(`${API_URL()}/libros/${isbn}`);
        if (response.ok) {
            const data = await response.json();
            document.getElementById('book-isbn').value = data.isbn;
            document.getElementById('book-title').value = data.titulo || '';
            document.getElementById('book-author').value = data.autor || '';
            document.getElementById('book-publisher').value = data.editorial || '';
            document.getElementById('book-category').value = data.categoria || '';
            document.getElementById('book-shelf').value = data.estanteria || '';
            document.getElementById('book-url').value = data.urlPortada || '';
            document.getElementById('scan-result').classList.remove('hidden');
        }
    } catch (error) {
        alert('Error al cargar datos del libro.');
    } finally {
        showLoader(false);
    }
}

async function loadDashboardStats() {
    try {
        // In a real app, you would have a /stats endpoint. 
        // Here we'll just fetch lists to count for demonstration purposes, 
        // but in production we'd want dedicated count endpoints.
        
        const [librosRes, sociosRes, prestamosRes] = await Promise.all([
            fetch(`${API_URL()}/libros`),
            fetch(`${API_URL()}/socios`),
            fetch(`${API_URL()}/prestamos/activos`)
        ]);

        if (librosRes.ok) {
            const libros = await librosRes.json();
            document.getElementById('stat-books').innerText = libros.length;
        }
        if (sociosRes.ok) {
            const socios = await sociosRes.json();
            document.getElementById('stat-users').innerText = socios.length;
        }
        if (prestamosRes.ok) {
            const prestamos = await prestamosRes.json();
            document.getElementById('stat-loans').innerText = prestamos.length;
        }
    } catch(e) {
        console.warn("Failed to load stats", e);
    }
}

// Initial load
window.addEventListener('DOMContentLoaded', () => {
    // Attempt to load stats on start (assuming default URL works)
    loadDashboardStats();
});
