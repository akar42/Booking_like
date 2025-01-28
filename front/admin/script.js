const reservations = [
    {
      reservationId: "TYH123",
      checkIn: "January 15, 2025",
      checkOut: "January 20, 2025",
      guests: 4,
      status: "Confirmed",
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
        status: "Confirmed",
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
        status: "Confirmed",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-15",
        endDate: "2025-01-20",
        adminId: "admin1"
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

  function populateAdminReservations(reservations) {
    const container = document.getElementById("reservations-container");
  
    container.innerHTML = "";

    const filteredReservations = filterReservationsByAdmin();

    if (filteredReservations.length === 0) {
      container.innerHTML = "<p>No reservations found for this admin.</p>";
      return;
    }
  
    filteredReservations.forEach((reservation) => {
      const reservationBlock = document.createElement("div");
      reservationBlock.classList.add("reservation-block");
  
      reservationBlock.innerHTML = `
        
        <div class="reservations">
          <div class="h2">
            <h2><strong>Reservation id:</strong> ${reservation.reservationId || "N/A"}</h2>
          </div>
          <div class="reservation-info">
            <div class="reservation-info1">
              <p><strong>Reservation id:</strong> ${reservation.reservationId || "N/A"}</p>
              <p><strong>Room id:</strong> ${reservation.roomId || "N/A"}</p>
              <p><strong>Total cost:</strong> ${reservation.totalCost || "N/A"}</p>
              <p><strong>Start date:</strong> ${reservation.startDate || "N/A"}</p>
              <p><strong>End date:</strong> ${reservation.endDate || "N/A"}</p>
              <p><strong>Admin id:</strong> ${reservation.adminId || "N/A"}</p>
            </div>
          </div>
          <div class="buttons">
            <button class="button">Accept reservation</button>
            <button class="button">Cancel reservation</button>
          </div>
        </div>
        
      `;
      container.appendChild(reservationBlock);
    });
  }
  


  function checkAdminAccess() {
    const isLoggedIn = getCookie("isLoggedIn");
    const userRole = getCookie("adminRole");

  if (isLoggedIn && userRole === "admin") {
    const filteredAdminReservations = filterReservationsByAdmin();
    populateAdminReservations(filteredAdminReservations);
  } else {
    alert("Access denied. Admin login required.");
    window.location.href = "login.html";
  }
  }
  document.addEventListener("DOMContentLoaded", checkAdminAccess);