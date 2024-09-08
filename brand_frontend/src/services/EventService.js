import axios from 'axios';

const EVENT_API_BASE_URL = "http://localhost:1110/api/v1/events";

class EventService {
    createEvent(event) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        console.log("accessToken", accessToken);

        return axios.post(`${EVENT_API_BASE_URL}`, event, {
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}`
            },
            withCredentials: true
        });
    }
    getAllEventsByBrandId(brandID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${EVENT_API_BASE_URL}/brand/all/${brandID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
    getEventsByBrandId(brandID, searchTerm, pageNumber, pageSize) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${EVENT_API_BASE_URL}/brand/${brandID}?pageNumber=${pageNumber}&pageSize=${pageSize}&searchTerm=${searchTerm}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getEventByEventId(eventID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${EVENT_API_BASE_URL}/${eventID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    updateEvent(eventID, event) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.put(`${EVENT_API_BASE_URL}/${eventID}`, event, {
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getEventsOfBrandHaveTargetWord(brandID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${EVENT_API_BASE_URL}/brand/have-target-word/${brandID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const eventServiceInstance = new EventService();

export default eventServiceInstance;