// Uçuş işlemleri için API servis fonksiyonları

export async function addFlight(flight) {
  const res = await fetch('http://localhost:8080/api/flights', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(flight),
  });
  if (!res.ok) throw new Error('Kayıt başarısız');
  return await res.json();
}

export async function getAirlines() {
  // Gerçek API ile değiştirilebilir
  return [
    { code: 'TK', name: 'Turkish Airlines' },
    { code: 'PC', name: 'Pegasus' },
  ];
}

export async function getAircraftTypes() {
  // Gerçek API ile değiştirilebilir
  return [
    { code: 'A320', name: 'Airbus A320' },
    { code: 'B738', name: 'Boeing 737-800' },
  ];
}

export async function getFlights() {
  const res = await fetch('http://localhost:8080/api/flights');
  if (!res.ok) throw new Error('Veri alınamadı');
  return await res.json();
}

export async function getStations() {
  return [
    { code: 'IST', name: 'İstanbul Havalimanı', country: 'Türkiye' },
    { code: 'ESB', name: 'Esenboğa', country: 'Türkiye' },
    { code: 'SAW', name: 'Sabiha Gökçen', country: 'Türkiye' },
    { code: 'ADB', name: 'Adnan Menderes', country: 'Türkiye' },
  ];
}

export async function getRoutes() {
  return [
    { origin: 'IST', destination: 'ESB' },
    { origin: 'SAW', destination: 'ADB' },
  ];
}

export async function getFlightTypes() {
  return [
    { code: 'PAX', name: 'Passenger' },
    { code: 'CRG', name: 'Cargo' },
    { code: 'POS', name: 'Position' },
  ];
} 