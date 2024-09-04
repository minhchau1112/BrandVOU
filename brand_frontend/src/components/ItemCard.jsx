import React from 'react';
import {NavLink, useNavigate} from 'react-router-dom';
import './EventCard.css';

const ItemCard = ({ item }) => {
    const navigate = useNavigate();

    const handleCardClick = () => {
        navigate(`/items/view-detail/${item.id}`);
    };

    const handleNavLinkClick = (event) => {
        event.stopPropagation();
    };

    return (
        <div className="event-card" onClick={handleCardClick}>
            <img src={item.image} alt={item.name} className="event-image" />
            <div className='d-flex flex-column justify-content-start'>
                <h3>{item.name}</h3>
                <p>Event:
                    <NavLink
                        to={`/events/view-detail/${item.event.id}`}
                        onClick={handleNavLinkClick}
                    >
                        {item.event.name}
                    </NavLink>
                </p>
            </div>
        </div>
    );
};

export default ItemCard;