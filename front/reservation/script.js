document.addEventListener("DOMContentLoaded", () => {
  const cookieData = getCookie("selectedReservation");

  if (cookieData) {
      try {
          const reservation = JSON.parse(decodeURIComponent(cookieData));

          if (!reservation) {
              console.error("No valid reservation found in cookies");
              return;
          }

          console.log("Loaded reservation:", reservation);
          populateFields(reservation);

          // Обработчик кнопки "Cancel Reservation"
          document.querySelector(".cancel").addEventListener("click", () => {
              cancelReservation(reservation.reservationId);
          });

          // Обработчик кнопки "Back to Profile"
          document.querySelector(".back").addEventListener("click", () => {
              window.location.href = "my_profile.html"; // Возвращаемся на личный кабинет
          });

      } catch (error) {
          console.error("Error parsing selectedReservation cookie:", error);
      }
  } else {
      console.error("No reservation data found in cookies");
  }
});


// === Функция получения cookie ===
function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(";").shift();
  return null;
}

// === Заполнение данных о бронировании ===
function populateFields(reservation) {
  if (!reservation) {
      console.error("Reservation data is missing:", reservation);
      return;
  }

  document.querySelector(".h2 h2").innerHTML = `<strong>Reservation id:</strong> ${reservation.reservationId || "N/A"}`;

  document.querySelector(".reservation-info1").innerHTML = `
      <p><strong>Room id:</strong> ${reservation.rooms ? reservation.rooms.map(r => r.roomId).join(", ") : "N/A"}</p>
      <p><strong>Total cost:</strong> ${reservation.totalCost || "N/A"}</p>
      <p><strong>Start date:</strong> ${reservation.startDate || "N/A"}</p>
      <p><strong>End date:</strong> ${reservation.endDate || "N/A"}</p>
      <p><strong>Status:</strong> ${reservation.status || "N/A"}</p>
  `;
}

// === Удаление бронирования ===
async function cancelReservation(reservationId) {
  if (!reservationId) {
      console.error("Reservation ID is missing.");
      alert("Error: Reservation ID not found.");
      return;
  }

  const authToken = localStorage.getItem("authToken");
  if (!authToken) {
      alert("You must log in to cancel a reservation.");
      return;
  }

  try {
      const response = await fetch(`http://localhost:8080/api/reservation/${reservationId}`, {
          method: "DELETE",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          }
      });

      if (!response.ok) {
          throw new Error(`Error - DELETE request: ${response.status}`);
      }

      alert("Reservation successfully canceled.");
      deleteCookie("selectedReservation"); // Удаляем cookie с данными бронирования
      window.location.href = "my_profile.html"; // Перенаправляем пользователя
  } catch (err) {
      console.error("Error canceling reservation:", err);
      alert("Failed to cancel the reservation. Try again later.");
  }
}

// === Удаление cookie ===
function deleteCookie(name) {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`;
}
