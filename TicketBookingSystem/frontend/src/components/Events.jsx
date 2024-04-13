import { useNavigate } from "react-router-dom";
import Navibar from "../components/navbar";
import EventsCard from "./EventsCard";
import axios from "axios";
import { useEffect, useState } from "react";
import { Form } from "react-bootstrap";

const Events = () =>{
    const [events, setEvents] = useState([]);
    const [uniqueEventTypes, setUniqueEventTypes] = useState([]);
    const [selectedEventTypes, setSelectedEventTypes] = useState([]);

    const handleCheckboxChange = (event) => {
        if (event.target.checked) {
            setSelectedEventTypes(prevState => [...prevState, event.target.value]);
        } else {
            setSelectedEventTypes(prevState => prevState.filter(eventType => eventType !== event.target.value));
        }
    };
    const navigate = useNavigate();
    useEffect(()=>{
        const fetchEvents = async () => {
            try{
                let api_endpoint_url='http://localhost:8080/event/viewAllEvents';
                const response = await axios.get(api_endpoint_url);
                setEvents(response.data);
                setUniqueEventTypes([...new Set(response.data.map(event => event.eventType.toLowerCase()))]);
                setSelectedEventTypes([...new Set(response.data.map(event => event.eventType.toLowerCase()))]);
            }catch(error){
                console.log("Error fetching events:", error);
            }
            
        };
        fetchEvents();
    },[]);

    function clickEvent(id){
        navigate("/event/"+id);
    }

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    return (
        <>
            <Navibar/>
            <div className="container">
                <section className="py-5 bg-light">
                        <div className="container">
                            <div className="row px-4 px-lg-5 py-lg-4 align-items-center">
                                <div className="col-lg-6">
                                    <h1 className="h2 text-uppercase mb-0">Home</h1>
                                </div>
                                <div className="col-lg-6 text-lg-end">
                                    <nav aria-label="breadcrumb">
                                        <ol className="breadcrumb justify-content-lg-end mb-0 px-0 bg-light">
                                            
                                        </ol>
                                    </nav>
                                </div>
                            </div>
                        </div>
                </section>
            </div>
            <br></br>
            <div className="container p-0">
                <div className="row">
                    <h1 className="h2">Events</h1>
                
                    <div className="col-lg-3 order 2 order-lg-1">
                        
                        <h6 className="text-uppercase mb-3">Event Type</h6>
                        <div className="form-check mb-1">
                        {uniqueEventTypes.map((eventType, index) => (
                                <Form.Check 
                                    key={index}
                                    type="switch"
                                    label={eventType}
                                    value={eventType}
                                    checked={selectedEventTypes.includes(eventType)}
                                    onChange={handleCheckboxChange}
                                />
                            ))}

                        </div>
                    </div>
                    <div className="col-lg-9 order-1 order-lg-2 mb-5 mb-lg-0">
                            
                            <div className="row">
                                
                            {(() => {
                                const filteredEvents = events.filter(event => selectedEventTypes.includes(event.eventType));

                                return filteredEvents.map(event => {
                                    const eventDate = new Date(event.dateTime);
                                    const today = new Date();
                                    
                                    return event.status === 'Active' && eventDate >= today && (
                                        <EventsCard 
                                            key={event.id} 
                                            eventName={event.eventName}
                                            imagelink={event.image}
                                            eventVenue={event.venue}
                                            eventDate={formatDate(eventDate)}
                                            onClick={(e)=>{
                                                clickEvent(event.id);
                                            }}
                                        />
                                    );
                                });
                            })()}
                            </div>
                            <h1 className="text-center mt-5">Past / Cancelled Events</h1>
                            <div className="row">
                            {events.filter(event => selectedEventTypes.includes(event.eventType)).map(event => {

                                    const eventDate = new Date(event.dateTime);
                                    const today = new Date();

                                    return (event.status !== 'Active' || (event.status === 'Active' && eventDate < today)) && (
                                        <EventsCard 
                                            key={event.id} 
                                            eventName={event.eventName}
                                            imagelink={event.image}
                                            eventVenue={event.venue}
                                            eventDate={formatDate(eventDate)}
                                            onClick={(e)=>{
                                                clickEvent(event.id);
                                            }}
                                        />
                                    );
                                })}
                            </div>
                        
                    </div>
                </div>
            </div>
            
        </>

    )
}
export default Events;