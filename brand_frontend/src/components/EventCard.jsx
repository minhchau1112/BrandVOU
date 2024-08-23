import React from 'react';
import { useNavigate } from 'react-router-dom';
import './EventCard.css';

const EventCard = ({ event }) => {
    const navigate = useNavigate();

    const handleCardClick = () => {
        navigate(`/events/view-detail/${event.id}`);
    };

    return (
        <div className="event-card" onClick={handleCardClick}>
            <img src={event.image} alt={event.name} className="event-image" />
            <div className='d-flex flex-column justify-content-start'>
                <h3>{event.name}</h3>
                <p>Time: {new Date(event.startTime).toLocaleDateString('vi-VN', { year: 'numeric', month: '2-digit', day: '2-digit' })} - {new Date(event.endTime).toLocaleDateString('vi-VN', { year: 'numeric', month: '2-digit', day: '2-digit' })}</p>

            </div>
        </div>
    );
};

export default EventCard;