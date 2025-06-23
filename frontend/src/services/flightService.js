import apiClient from './api';

// #region Flight Operations
export async function addFlight(flight) {
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
  const response = await apiClient.post('/api/flights', flightDTO);
  return response.data;
}

export async function getFlights() {
  const response = await apiClient.get('/api/flights');
  return response.data;
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
  const response = await apiClient.put(`/api/flights/${id}`, flightDTO);
  return response.data;
}

export async function deleteFlight(id) {
  await apiClient.delete(`/api/flights/${id}`);
}
// #endregion

// #region Generic Reference Data Functions
async function getRefData(endpoint) {
  const response = await apiClient.get(`/api/${endpoint}`);
  return response.data;
}

async function addRefData(endpoint, data) {
  const response = await apiClient.post(`/api/${endpoint}`, data);
  return response.data;
}

async function deleteRefData(endpoint, id) {
  await apiClient.delete(`/api/${endpoint}/${id}`);
}
// #endregion

// #region Specific Reference Data Exports
export const getAirlines = () => getRefData('airlines');
export const addAirline = (data) => addRefData('airlines', data);
export const deleteAirline = (code) => deleteRefData('airlines', code);

export const getAircraftTypes = () => getRefData('aircraft-types');
export const addAircraftType = (data) => addRefData('aircraft-types', data);
export const deleteAircraftType = (code) => deleteRefData('aircraft-types', code);

export const getStations = () => getRefData('stations');
export const addStation = (data) => addRefData('stations', data);
export const deleteStation = (code) => deleteRefData('stations', code);

export const getRoutes = () => getRefData('routes');
export const addRoute = (data) => addRefData('routes', data);
export const deleteRoute = (id) => deleteRefData('routes', id);

export const getFlightTypes = () => getRefData('flight-types');
export const addFlightType = (data) => addRefData('flight-types', data);
export const deleteFlightType = (code) => deleteRefData('flight-types', code);
// #endregion
