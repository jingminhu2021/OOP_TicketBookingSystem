import { useEffect, useState } from "react";
import Navibar from "../components/navbar";
import { useParams } from "react-router-dom";
import DisplayEventDetail from "../components/DisplayEventDetail";
import axios from "axios";

export default function EventDetailPage(){
    const [eventData, setEventData]=useState();
    const eventId = useParams().event_id;
    console.log(useParams().event_id);
    useEffect(()=>{
        const fetchEvent = async () =>{
            try{
                let api_endpoint_url='http://localhost:8080/event/viewEvent';
                const response = await axios.get(api_endpoint_url,{
                    params:{
                        id: eventId
                    }
                });
                setEventData(response.data);
                console.log(response.data);
            }catch(error){
                console.log("Error fetching event details:",error);
            }
        };
        fetchEvent();
    },[]);
    if (eventData){
        return(
            <>
                <Navibar/>
                <DisplayEventDetail eventData={eventData}/>
            </>
        )
    }

}