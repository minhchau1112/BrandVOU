import axios from 'axios';

const EVENT_API_BASE_URL = "http://localhost:8080/api/v1/events";

class EventService {

    getEvent(){
        return axios.get(EVENT_API_BASE_URL);
    }

    createEvent(event){
        return axios.post(EVENT_API_BASE_URL, event);
    }

    getEventById(eventId){
        return axios.get(EVENT_API_BASE_URL + '/' + eventId);
    }

    updateEvent(event, eventId){
        return axios.put(EVENT_API_BASE_URL + '/' + eventId, event);
    }

    deleteEvent(eventId){
        return axios.delete(EVENT_API_BASE_URL + '/' + eventId);
    }
}

export default new EventService()