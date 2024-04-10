const EventsCard = ({
    imagelink,
    eventName,
    eventDate,
    eventVenue,
    onClick,
})=> {
    return(
    <div className="col-lg-4 col-sm-6"onClick={onClick}>
        <div className="product text-center">
          <div className="mb-3 position-relative">
            <div className="badge text-white bg-primary">Event Date:{eventDate}</div><a className="d-block"><img className="img-fluid" style={{width: '100%', height: '300px'}}  src={imagelink}alt="..."/></a>
            <div className="product-overlay">
              {/* <ul class="mb-0 list-inline">
                <li class="list-inline-item m-0 p-0"><a class="btn btn-sm btn-outline-dark" href="#!"><i class="far fa-heart"></i></a></li>
                <li class="list-inline-item m-0 p-0"><a class="btn btn-sm btn-dark" href="cart.html">Add to cart</a></li>
                <li class="list-inline-item mr-0"><a class="btn btn-sm btn-outline-dark" href="#productView" data-bs-toggle="modal"><i class="fas fa-expand"></i></a></li>
              </ul> */}
            </div>
          </div>
          <h6> <a className="reset-anchor" onClick={onClick}>{eventName}</a></h6>
          <p className="small text-muted">Venue: {eventVenue}</p>
        </div>
    </div>
    )
};
export default EventsCard;