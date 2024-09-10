import axios from 'axios';

const EVENTGAMES_API_BASE_URL = "http://localhost:1110/api/v1/events/eventgames";

class EventGamesService {
    findGameNamesByEventId(eventId) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${EVENTGAMES_API_BASE_URL}/games-name?eventId=${eventId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const eventgamesServiceInstance = new EventGamesService();

export default eventgamesServiceInstance;