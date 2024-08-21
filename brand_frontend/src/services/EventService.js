import axios from 'axios';

const EVENT_API_BASE_URL = "http://localhost:9090/api/v1/events";

class EventService {
    getEvent() {
        return axios.get(EVENT_API_BASE_URL);
    }

    createEvent(event, brandID) {
        return axios.post(`${EVENT_API_BASE_URL}/${brandID}`, event, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }

    getEventsByBrandId(brandID) {
        return axios.get(`${EVENT_API_BASE_URL}/${brandID}`);
    }

    updateEvent(event, eventId) {
        return axios.put(`${EVENT_API_BASE_URL}/${eventId}`, event);
    }
}

const eventServiceInstance = new EventService();

export default eventServiceInstance;