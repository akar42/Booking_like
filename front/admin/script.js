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
        dataContainer.innerHTML += "<p>No reservations found.</p>";
        return;
    }

    reservations.forEach((reservation) => {
        const reservationBlock = document.createElement("div");
        reservationBlock.classList.add("reservation-block");

        // Формируем список комнат внутри бронирования
        let roomsInfo = "";
        if (reservation.rooms && reservation.rooms.length > 0) {
            roomsInfo = reservation.rooms.map(room => `
                <div class="room-info">
                    <p><strong>Room ID:</strong> ${room.roomId}</p>
                    <p><strong>Room Number:</strong> ${room.roomNumber}</p>
                    <p><strong>Type:</strong> ${room.roomType}</p>
                    <p><strong>Price:</strong> ${room.price} $</p>
                    <p><strong>Guests:</strong> ${room.guestNumber}</p>
                    <p><strong>Floor:</strong> ${room.floorNumber}</p>
                    <p><strong>Facilities:</strong> ${room.facilities}</p>
                </div>
            `).join("");
        } else {
            roomsInfo = "<p>No rooms assigned.</p>";
        }

        reservationBlock.innerHTML = `
            <div class="reservation">
                <p><strong>Reservation ID:</strong> ${reservation.reservationId}</p>
                <p><strong>User ID:</strong> ${reservation.userId}</p>
                <p><strong>Total Cost:</strong> ${reservation.totalCost} $</p>
                <p><strong>Start Date:</strong> ${reservation.startDate}</p>
                <p><strong>End Date:</strong> ${reservation.endDate}</p>
                <p><strong>Status:</strong> ${reservation.status}</p>
                <p><strong>Admin ID:</strong> ${reservation.adminId || "Not assigned"}</p>
                <div class="rooms-container">
                    <h4>Rooms:</h4>
                    ${roomsInfo}
                </div>
                <div class="buttons">
                    <button class="button" id="pick-me-${reservation.reservationId}">Pick reservation</button>
                </div>
            </div>
        `;

        dataContainer.appendChild(reservationBlock);

        document.getElementById(`pick-me-${reservation.reservationId}`).addEventListener("click", function () {
            handlePickMe(reservation);
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

    // const currentAdminId = getCurrentAdminId();
    // const filteredReservations = adminReservations.filter(reservation => reservation.adminId === currentAdminId);

    // if (filteredReservations.length === 0) {
    //     dataContainer.innerHTML += "<p>Brak rezerwacji dla tego admina.</p>";
    //     return;
    // }

    adminReservations.forEach((reservation) => {
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
          handleAccept(reservation);
        });

        document.getElementById(`cancel-${reservation.reservationId}`).addEventListener('click', function () {
          handleCancel(reservation);
        });
    });
}

async function handlePickMe(reservation) {
  const authToken = localStorage.getItem("authToken");

  if (!authToken) {
      alert("You must log in as an admin.");
      return;
  }

  try {
      // 1. Запрос к /api/admin/logged для получения adminId
      const adminResponse = await fetch("http://localhost:8080/api/admin/logged", {
          method: "GET",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          }
      });

      if (!adminResponse.ok) {
          throw new Error(`Error - GET admin: ${adminResponse.status}`);
      }

      const adminData = await adminResponse.json();
      const adminId = adminData.adminId; // Получаем adminId

      console.log(adminData);


      // 2. Обновляем adminId в переданной резервации
      const updatedReservation = { ...reservation, adminId: adminId };

      // 3. PUT запрос на /api/reservation/{id} с обновленной резервацией
      const updateResponse = await fetch(`http://localhost:8080/api/reservation/${Number(reservation.reservationId)}`, {
          method: "PUT",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          },
          body: JSON.stringify(updatedReservation)
      });

      if (!updateResponse.ok) {
          throw new Error(`Error - PUT reservation: ${updateResponse.status}`);
      }

      alert(`Reservation ${reservation.reservationId} successfully assigned to admin ${adminId}!`);

      // 4. Перенос резервации в список админа и обновление отображения
      reservations = reservations.filter(res => res.reservationId !== reservation.reservationId);
      adminReservations.push(updatedReservation);

      populateAdminReservations(adminReservations);
      populateAllReservations(reservations);
  } catch (err) {
      console.error("Error processing reservation:", err);
      alert("Failed to assign reservation. Try again later.");
  }
}



async function handleAccept(reservation) {
  await updateReservationStatus(reservation, "Confirmed");
}

async function handleCancel(reservation) {
  await updateReservationStatus(reservation, "Cancelled");
}

async function updateReservationStatus(reservation, newStatus) {
  const authToken = localStorage.getItem("authToken");

  if (!authToken) {
      alert("You must log in as an admin.");
      return;
  }

  try {
      // 1. Обновляем статус в копии резервации
      const updatedReservation = { ...reservation, status: newStatus };

      // 2. Отправляем PUT-запрос на сервер
      const response = await fetch(`http://localhost:8080/api/reservation/${reservation.reservationId}`, {
          method: "PUT",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          },
          body: JSON.stringify(updatedReservation)
      });

      if (!response.ok) {
          throw new Error(`Error - PUT reservation: ${response.status}`);
      }

      alert(`Reservation ${reservation.reservationId} successfully updated to "${newStatus}"!`);

      // 3. Обновляем отображение резерваций
      fetchAdminReservations();
  } catch (err) {
      console.error("Error updating reservation:", err);
      alert("Failed to update reservation. Try again later.");
  }
}

async function fetchAllReservations() {
  const authToken = localStorage.getItem("authToken");
  if (!authToken) {
      alert("You must log in as an admin to view reservations.");
      return;
  }

  try {
      const response = await fetch("http://localhost:8080/api/reservations", {
          method: "GET",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          }
      });

      if (!response.ok) {
          throw new Error(`Error - GET request: ${response.status}`);
      }

      const reservationsData = await response.json();
      console.log("Fetched Reservations:", reservationsData);
      populateAllReservations(reservationsData); // Заполняем UI полученными данными
  } catch (err) {
      console.error("Error fetching reservations:", err);
      alert("Failed to load reservations. Try again later.");
  }
}

async function fetchAdminReservations() {
  const authToken = localStorage.getItem("authToken");

  if (!authToken) {
      alert("You must log in as an admin.");
      return;
  }

  try {
      // 1. Получаем adminId
      const adminResponse = await fetch("http://localhost:8080/api/admin/logged", {
          method: "GET",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          }
      });

      if (!adminResponse.ok) {
          throw new Error(`Error - GET admin: ${adminResponse.status}`);
      }

      const adminData = await adminResponse.json();
      const adminId = adminData.adminId; 

      console.log("Admin Data:", adminData);

      const reservationsData = adminData.reservations;
      console.log("Fetched Admin Reservations:", reservationsData);

      // 3. Заполняем страницу полученными резервациями
      populateAdminReservations(reservationsData);

  } catch (err) {
      console.error("Error fetching admin reservations:", err);
      alert("Failed to load admin reservations. Try again later.");
  }
}

function checkAdminAccess() {
  const token = localStorage.getItem("authToken");
  const userRole = localStorage.getItem("userRole");

  if (token && userRole === "ROLE_ADMIN") {
      const container = document.getElementById("reservations-container");
      container.innerHTML = `
          <button class="button reservation-view" id="show-all">Show all reservations</button>
          <button class="button reservation-view" id="show-my">Show my reservations</button>
      `;

      document.getElementById("show-all").addEventListener("click", fetchAllReservations);
      // document.getElementById("show-my").addEventListener("click", function () {
      //     populateAdminReservations(adminReservations);
      // });

      document.getElementById("show-my").addEventListener("click", fetchAdminReservations);
  } else {
      alert("Access denied. Admin login required.");
      window.location.href = "login.html";
  }
}

document.addEventListener("DOMContentLoaded", checkAdminAccess);

