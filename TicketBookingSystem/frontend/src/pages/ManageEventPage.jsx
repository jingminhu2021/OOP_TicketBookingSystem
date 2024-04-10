import { useEffect, useState} from "react";
import Navibar from "../components/navbar";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import AddEvent from '../components/event/addEvent';
import UpdateEvent from '../components/event/updateEvent';
import CancelEvent from '../components/event/cancelEvent';
import CreateTicketType from '../components/event/createTicketType';
import ViewTicketOptionsForManager from "../components/event/viewTicketOptionsForManager";
import AddTicketOfficerToEvent from "../components/event/addTicketOfficerToEvent";
import { Table } from 'react-bootstrap';

function EventPage(){
    const [events, setEvents] = useState([]
    );
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };

    useEffect(() => {
        const getManagerEvents = async () => {
            let api_endpoint_url = 'http://localhost:8080/event/viewOwnEventByEventManager';
            const bodyParameters = {
            };
            try {
                const response = await axios.post(api_endpoint_url,bodyParameters,config);
                var item = response.data.events;
                for (let i = 0; i < item.length; i++) {
                    // console.log(item[i].status)
                    item[i].status = item[i].status === 'Active' && new Date(item[i].dateTime[0], item[i].dateTime[1] - 1, ...item[i].dateTime.slice(2)) < new Date() ? 'Ended' : item[i].status
                }
                setEvents(item);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };
        if (token) {
            getManagerEvents(token);
        }
    }, []); 


    function checkHasEvents(){
        if(events.length > 0){
            // console.log("current events:",events);
            return(
                <div class="row">
                    <Table hover>
                        <thead>
                            <tr>
                                <th>Event ID</th>
                                <th>Event Name</th>
                                <th>Event Type</th>
                                <th>Event Start Date</th>
                                <th>Event Venue</th>
                                <th>Event Description</th>
                                <th>Event Status</th>
                                <th>Update Event</th>
                                <th>View/Add Ticket Type</th>
                                <th>Cancel Event</th>
                                <th>View/Add Ticketing Officer</th>
                            </tr>
                        </thead>
                        <tbody>
                            {events.map((event, index) => {
                            // console.log(ticketOption)
                            return(
                                <tr key={index}>
                                    <td>{event.id}</td>
                                    <td>{event.eventName}</td>
                                    <td>{event.eventType}</td>
                                    <td>{new Date(event.dateTime[0], event.dateTime[1] - 1, ...event.dateTime.slice(2)).toLocaleString()}</td>
                                    <td>{event.venue}</td>
                                    <td>{event.description}</td>
                                    <td>
                                        {event.status}
                                    </td>
                                    <td><UpdateEvent event_id={event.id} event_status={event.status}/></td>
                                    <td><ViewTicketOptionsForManager event_id={event.id} event_status={event.status} /></td>
                                    <td><CancelEvent event_id={event.id} event_status={event.status}/></td>
                                    <td><AddTicketOfficerToEvent event_id={event.id} event_status={event.status} event_name={event.eventName}/></td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </Table>
                </div>
            )
        }else{
            return(
                <a>You currently have zero event, please add a new event</a>)
        }
    }
    const navigate = useNavigate();
    return(
        <>
            <Navibar/>
            <div className="container">
                <section className="py-5 bg-light">
                    <div className="container">
                        <div className="row px-4 px-lg-5 py-lg-4 align-items-center">
                            <div className="col-lg-6">
                                <h1 className="h2 text-uppercase mb-0">Manage Events</h1>
                            </div>
                            <div className="col-lg-6 text-lg-end">
                                <nav aria-label="breadcrumb">
                                    <ol className="breadcrumb justify-content-lg-end mb-0 px-0 bg-light">
                                        <li className="breadcrumb-item"><a className="text-dark" href="/">Home</a></li>
                                        <li className="breadcrumb-item active">Manage Events</li>
                                    </ol>
                                </nav>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
            <div className="container px-lg-3 mt-4">
                
            
            {AddEvent()}
            
            <br></br>
            {checkHasEvents()}
            </div>
        </>
    )
}
export default EventPage;