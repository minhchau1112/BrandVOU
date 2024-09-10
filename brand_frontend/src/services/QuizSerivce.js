import axios from 'axios';

const QUIZ_API_BASE_URL = "http://localhost:1110/api/v1/events/quiz-question";

class QuizService {
    getRowQuizQuestionResponse(eventId, gameId) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${QUIZ_API_BASE_URL}/event-games/existed?eventId=${eventId}&gameId=${gameId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const quizServiceInstance = new QuizService();

export default quizServiceInstance;