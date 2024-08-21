import React, { useState, useEffect } from 'react';
import EventCard from './EventCard';
import EventService from '../services/EventService';
import './EventList.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

const EventList = ({ brandID }) => {
    const [events, setEvents] = useState([]);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredEvents, setFilteredEvents] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const response = await EventService.getEventsByBrandId(brandID);
                setEvents(response.data);
                setFilteredEvents(response.data);
                setError(null);
            } catch (error) {
                if (error.response) {
                    setError(`Server responded with an error: ${error.response.statusText}`);
                } else if (error.request) {
                    setError("No response from server.");
                } else {
                    setError(`Error: ${error.message}`);
                }
            }
        };

        fetchEvents();
    }, [brandID]);

    const handleSearch = () => {
        const results = events.filter(event =>
            event.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredEvents(results);
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    return (
        <div className="event-page">
            <div className="search-bar mb-4">
                <div className="input-group">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search events..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        onKeyDown={handleKeyDown} 
                    />
                    <button className="btn btn-outline-secondary" type="button" onClick={handleSearch}>
                        <FontAwesomeIcon icon={faSearch} />
                    </button>
                </div>
                <button className="btn btn-primary mt-3" onClick={() => window.location.href = `/add-event`}>
                    Add event
                </button>
            </div>
            {error && <div className="error-message">{error}</div>}
            <div className="event-list">
                {filteredEvents.length > 0 ? (
                    filteredEvents.map(event => (
                        <EventCard key={event.id} event={event} />
                    ))
                ) : (
                    <div>No events found.</div>
                )}
            </div>
        </div>
    );
};

export default EventList;
