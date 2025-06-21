// Uçuş işlemleri için API servis fonksiyonları

const API_BASE_URL = 'http://localhost:8080/api';

// #region Flight Operations
export async function addFlight(flight) {
  // DTO'ya uygun hale getir
  const flightDTO = {
    flightNumber: flight.flightNumber,
    airlineCode: flight.airline,
    aircraftTypeCode: flight.aircraftType,
    originCode: flight.origin,
    destinationCode: flight.destination,
    flightDate: flight.date,
    std: flight.std,
    sta: flight.sta,
    flightType: flight.flightType,
    delay: flight.delay,
    status: flight.status,
  };

  const res = await fetch(`${API_BASE_URL}/flights`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(flightDTO),
  });
  if (!res.ok) throw new Error('Uçuş kaydı başarısız');
  return await res.json();
}

export async function getFlights() {
  const res = await fetch(`${API_BASE_URL}/flights`);
  if (!res.ok) throw new Error('Uçuş verileri alınamadı');
  return await res.json();
}

export async function updateFlight(id, flightData) {
  const flightDTO = {
    flightNumber: flightData.flightNumber,
    airlineCode: flightData.airline,
    aircraftTypeCode: flightData.aircraftType,
    originCode: flightData.origin,
    destinationCode: flightData.destination,
    flightDate: flightData.date,
    std: flightData.std,
    sta: flightData.sta,
    flightType: flightData.flightType,
    delay: flightData.delay,
    status: flightData.status,
  };

  const res = await fetch(`${API_BASE_URL}/flights/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(flightDTO),
  });
  if (!res.ok) throw new Error('Uçuş güncellenemedi');
  return await res.json();
}

export async function deleteFlight(id) {
    const res = await fetch(`${API_BASE_URL}/flights/${id}`, {
        method: 'DELETE',
    });
    if (!res.ok) throw new Error('Uçuş silinemedi');
}
// #endregion

// #region Reference Data
async function fetchReferenceData(endpoint) {
  const res = await fetch(`${API_BASE_URL}/reference/${endpoint}`);
  if (!res.ok) throw new Error(`Referans veri alınamadı: ${endpoint}`);
  return await res.json();
}

export async function getAirlines() {
  return fetchReferenceData('airlines');
}

export async function addAirline(airline) {
  const res = await fetch(`${API_BASE_URL}/reference/airlines`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(airline),
  });
  if (!res.ok) throw new Error('Havayolu eklenemedi');
  return await res.json();
}

export async function deleteAirline(code) {
  const res = await fetch(`${API_BASE_URL}/reference/airlines/${code}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Havayolu silinemedi');
}

export async function getAircraftTypes() {
  return fetchReferenceData('aircraft-types');
}

export async function addAircraftType(type) {
  const res = await fetch(`${API_BASE_URL}/reference/aircraft-types`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(type),
  });
  if (!res.ok) throw new Error('Uçak tipi eklenemedi');
  return await res.json();
}

export async function deleteAircraftType(code) {
  const res = await fetch(`${API_BASE_URL}/reference/aircraft-types/${code}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Uçak tipi silinemedi');
}

export async function getStations() {
  return fetchReferenceData('stations');
}

export async function addStation(station) {
  const res = await fetch(`${API_BASE_URL}/reference/stations`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(station),
  });
  if (!res.ok) throw new Error('Havalimanı eklenemedi');
  return await res.json();
}

export async function deleteStation(code) {
  const res = await fetch(`${API_BASE_URL}/reference/stations/${code}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Havalimanı silinemedi');
}

export async function getRoutes() {
  return fetchReferenceData('routes');
}

export async function addRoute(route) {
  const res = await fetch(`${API_BASE_URL}/reference/routes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(route),
  });
  if (!res.ok) throw new Error('Rota eklenemedi');
  return await res.json();
}

export async function deleteRoute(id) {
  const res = await fetch(`${API_BASE_URL}/reference/routes/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Rota silinemedi');
}

export async function getFlightTypes() {
  return fetchReferenceData('flight-types');
}

export async function addFlightType(type) {
  const res = await fetch(`${API_BASE_URL}/reference/flight-types`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(type),
  });
  if (!res.ok) throw new Error('Uçuş tipi eklenemedi');
  return await res.json();
}

export async function deleteFlightType(code) {
  const res = await fetch(`${API_BASE_URL}/reference/flight-types/${code}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Uçuş tipi silinemedi');
}
// #endregion 