let adminReservations = [
    {
      reservationId: "TYH123",
      checkIn: "January 15, 2025",
      checkOut: "January 20, 2025",
      guests: 4,
      status: "",
      roomId: "123",
      totalCost: "$500",
      startDate: "2025-01-15",
      endDate: "2025-01-20",
      adminId: "admin2"
    },
    {
        reservationId: "252",
        checkIn: "January 20, 2025",
        checkOut: "January 20, 2025",
        guests: 4,
        status: "",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-20",
        endDate: "2025-01-25",
        adminId: "admin1"
      },
      {
        reservationId: "123123",
        checkIn: "January 15, 2025",
        checkOut: "January 20, 2025",
        guests: 4,
        status: "",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-15",
        endDate: "2025-01-20",
        adminId: "admin1"
      },
    // ... 
  ];

  let reservations = [
    {
      reservationId: "abc",
      checkIn: "January 15, 2025",
      checkOut: "January 20, 2025",
      guests: 4,
      status: "",
      roomId: "123",
      totalCost: "$500",
      startDate: "2025-01-15",
      endDate: "2025-01-20",
      adminId: ""
    },
    {
        reservationId: "bcd",
        checkIn: "January 20, 2025",
        checkOut: "January 20, 2025",
        guests: 4,
        status: "",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-20",
        endDate: "2025-01-25",
        adminId: ""
      },
      {
        reservationId: "bbb",
        checkIn: "January 15, 2025",
        checkOut: "January 20, 2025",
        guests: 4,
        status: "",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-15",
        endDate: "2025-01-20",
        adminId: ""
      },
    // ... 
  ];

  function getCookie(name) {
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookies = decodedCookie.split("; ");
    for (const cookie of cookies) {
      const [key, value] = cookie.split("=");
      if (key === name) {
        return value;
      }
    }
    return null;
  }

  function getCurrentAdminId() {
    return getCookie("adminId");
  }

  function filterReservationsByAdmin() {
    const currentAdminId = getCurrentAdminId();
  
    if (!currentAdminId) {
      alert("You must log in as an admin to view reservations.");
      return [];
    }
  
    return reservations.filter(
      (reservation) => reservation.adminId === currentAdminId
    );
  }

  
  function populateAllReservations(reservations) {
    const container = document.getElementById("reservations-container");
 
    let dataContainer = document.getElementById("data-container");
    if (!dataContainer) {
        dataContainer = document.createElement("div");
        dataContainer.id = "data-container";
        container.appendChild(dataContainer);
    }
    dataContainer.innerHTML = ""; 

    if (reservations.length === 0) {
        dataContainer.innerHTML += "<p>Brak rezerwacji.</p>";
        return;
    }

   
    reservations.forEach((reservation) => {
        const reservationBlock = document.createElement("div");
        reservationBlock.classList.add("reservation-block");

        reservationBlock.innerHTML = `
            <div class="reservation">
                <p><strong>Reservation ID:</strong> ${reservation.reservationId}</p>
                <p><strong>Room ID:</strong> ${reservation.roomId}</p>
                <p><strong>Total Cost:</strong> ${reservation.totalCost}</p>
                <p><strong>Start Date:</strong> ${reservation.startDate}</p>
                <p><strong>End Date:</strong> ${reservation.endDate}</p>
                <div class="buttons">
                    <button class="button" id="pick-me-${reservation.reservationId}">Pick reservation</button>
                </div>
            </div>
        `;
        dataContainer.appendChild(reservationBlock);

        document.getElementById(`pick-me-${reservation.reservationId}`).addEventListener('click', function () {
          handlePickMe(reservation.reservationId);
      });
    });
}


function populateAdminReservations(adminReservations) {
    const container = document.getElementById("reservations-container");
   
    let dataContainer = document.getElementById("data-container");
    if (!dataContainer) {
        dataContainer = document.createElement("div");
        dataContainer.id = "data-container";
        container.appendChild(dataContainer);
    }
    dataContainer.innerHTML = ""; 

    const currentAdminId = getCurrentAdminId();
    const filteredReservations = adminReservations.filter(reservation => reservation.adminId === currentAdminId);

    if (filteredReservations.length === 0) {
        dataContainer.innerHTML += "<p>Brak rezerwacji dla tego admina.</p>";
        return;
    }

    filteredReservations.forEach((reservation) => {
        const reservationBlock = document.createElement("div");
        reservationBlock.classList.add("reservation-block");

        reservationBlock.innerHTML = `
            <div class="reservation">
                <p><strong>Reservation ID:</strong> ${reservation.reservationId}</p>
                <p><strong>Room ID:</strong> ${reservation.roomId}</p>
                <p><strong>Total Cost:</strong> ${reservation.totalCost}</p>
                <p><strong>Start Date:</strong> ${reservation.startDate}</p>
                <p><strong>End Date:</strong> ${reservation.endDate}</p>
                <p><strong>Status:</strong> ${reservation.status}</p>
                <p><strong>Admin ID:</strong> ${reservation.adminId}</p>
                <div class="buttons">
                  <button class="button" id="accept-${reservation.reservationId}">Accept reservation</button>
                  <button class="button" id="cancel-${reservation.reservationId}">Cancel reservation</button>
                </div>
            </div>
        `;
        dataContainer.appendChild(reservationBlock);

        document.getElementById(`accept-${reservation.reservationId}`).addEventListener('click', function () {
          handleAccept(reservation.reservationId);
        });

        document.getElementById(`cancel-${reservation.reservationId}`).addEventListener('click', function () {
          handleCancel(reservation.reservationId);
        });
    });
}

function handlePickMe(reservationId) {
  const currentAdminId = getCurrentAdminId();
  if (!currentAdminId) {
      alert("Nie jesteś zalogowany jako admin.");
      return;
  }

  const reservation = reservations.find(res => res.reservationId === reservationId);
  
  
  if (reservation && !reservation.adminId) {
      reservation.adminId = currentAdminId; 

      adminReservations.push(reservation);

    
      const index = reservations.findIndex(res => res.reservationId === reservationId);
      if (index > -1) {
          reservations.splice(index, 1); 
      }

      alert(`Rezerwacja ${reservationId} została przypisana Tobie!`);
      populateAdminReservations(adminReservations); 
      populateAllReservations(reservations);  
  } else {
      alert("Ta rezerwacja została już przypisana do innego admina.");
  }
}


function handleAccept(reservationId) {
  const reservation = adminReservations.find(res => res.reservationId === reservationId);
  if (reservation) {
      reservation.status = "Confirmed"; 
      alert(`Rezerwacja ${reservationId} została zatwierdzona!`);
      populateAdminReservations(adminReservations); 
     
      const allReservation = reservations.find(res => res.reservationId === reservationId);
      if (allReservation) {
          allReservation.status = "Confirmed";
      }
      
  }
}

function handleCancel(reservationId) {
  const reservation = adminReservations.find(res => res.reservationId === reservationId);
  if (reservation) {
      reservation.status = "Cancelled"; 
      alert(`Rezerwacja ${reservationId} została anulowana!`);
      populateAdminReservations(adminReservations); 
     
      const allReservation = reservations.find(res => res.reservationId === reservationId);
      if (allReservation) {
          allReservation.status = "Cancelled";
      }
      
  }
}


function checkAdminAccess() {
  const isLoggedIn = getCookie("isLoggedIn");
  const userRole = getCookie("adminRole");

  if (isLoggedIn && userRole === "admin") {
      const container = document.getElementById("reservations-container");
      container.innerHTML = `
          <button class="button reservation-view" id="show-all">Show all reservations</button>
          <button class="button reservation-view" id="show-my">Show my reservations</button>
      `;
      
      document.getElementById("show-all").addEventListener('click', function () {
          populateAllReservations(reservations);
      });

      document.getElementById("show-my").addEventListener('click', function () {
          populateAdminReservations(adminReservations);
      });

      
  } else {
      alert("Dostęp zabroniony. Wymagane logowanie jako admin.");
      window.location.href = "login.html";
  }
}

document.addEventListener("DOMContentLoaded", checkAdminAccess);
