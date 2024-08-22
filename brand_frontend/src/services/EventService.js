import axios from 'axios';

const EVENT_API_BASE_URL = "http://localhost:9090/api/v1/events";

class EventService {
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

    getEventByEventId(eventID) {
        return axios.get(`${EVENT_API_BASE_URL}/view-detail/${eventID}`);
    }

    updateEvent(event, eventId) {
        return axios.put(`${EVENT_API_BASE_URL}/update/${eventId}`, event, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }
}

const eventServiceInstance = new EventService();

export default eventServiceInstance;