import React from 'react';
import {NavLink, useNavigate} from 'react-router-dom';
import './ItemCard.css';
import ThreeDText from "./ThreeDText";

const ItemCard = ({ item }) => {
    const navigate = useNavigate();

    const handleCardClick = () => {
        navigate(`/items/view-detail/${item.id}`);
    };

    const handleNavLinkClick = (item) => {
        item.stopPropagation();
    };

    return (
        <div className="item-card" onClick={handleCardClick} style={{width: "100px !important"}}>
            <div className="d-flex justify-content-around">
                <ThreeDText text={item.name} size="text-6xl" />
            </div>
            <div className='d-flex flex-column justify-content-start'>
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