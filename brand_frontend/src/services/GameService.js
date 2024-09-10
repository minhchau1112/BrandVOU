import axios from 'axios';

const GAME_API_BASE_URL = "http://localhost:1110/api/v1/events/games";

class GameService {
    getAllGames() {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${GAME_API_BASE_URL}/all`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const gameServiceInstance = new GameService();

export default gameServiceInstance;