import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table } from 'react-bootstrap';
import UpdateTicketType from './updateTicketType';

function ViewTicketOptionsForManager(event_id) {
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [ticketOptions, setTicketOptions] = useState([]);

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {};
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setUserData(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        const getTicketOptions = async (event_id) => {
            let api_endpoint_url = 'http://localhost:8080/ticketType/viewTicketTypes';
            
            const bodyParameters = {
                event_id: event_id
            };

            axios.post(api_endpoint_url, bodyParameters, config)
            .then((response) => {
                setTicketOptions(prevResults => [...prevResults, ...response.data]);
            })
            .catch((error) => {
                console.error('Error occurred:', error);
            });
            
        }

        if (token) {
            getUserData(token);
            getTicketOptions(event_id);
            
        }
        
    }, []); // Empty dependency array ensures this effect runs only once

    

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Table>
                <thead>
                    <tr>
                        <th>Event Category</th>
                        <th>Event Price</th>
                        <th>Number of Tickets left</th>
                        <th>Cancellation Fee Percentage</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>

                    {ticketOptions.map((ticketOption, index) => {
                        console.log(ticketOption)
                        return(
                            <tr key={index}>
                                <td>{ticketOption.eventCat}</td>
                                <td>{ticketOption.eventPrice}</td>
                                <td>{ticketOption.numberOfTix}</td>
                                <td>{ticketOption.cancellationFeePercentage}</td>
                                <td>
                                    <UpdateTicketType ticketId={ticketOption.ticketTypeId} />
                                    {/* <Button variant="danger" onClick={() => {}}>Delete</Button> */}
                                </td>
                            </tr>
                        )
                    })}
                </tbody>
            </Table>
            )}
        </>
    )
}
export default ViewTicketOptionsForManager;