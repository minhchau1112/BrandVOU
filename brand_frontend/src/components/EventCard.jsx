import React from 'react';
import './EventCard.css';

const EventCard = ({ event }) => {
    return (
        <div className="event-card">
            <img src={event.image} alt={event.name} className="event-image" />
            <h3>{event.name}</h3>
            <p>Number of voucher: {event.voucherCount}</p>
            <p>Start: {new Date(event.startTime).toLocaleString()}</p>
            <p>End: {new Date(event.endTime).toLocaleString()}</p>
        </div>
    );
};

export default EventCard;