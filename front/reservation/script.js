function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(";").shift();
}

const cookieData = getCookie("selectedReservation");

if (cookieData) {
  const selectedData = JSON.parse(decodeURIComponent(cookieData));

  const { reservation, user } = selectedData;
  populateFields(reservation, user);
} else {
  console.error("No reservation or user data found in cookies");
}

function populateFields(reservation, user) {
  const header = document.querySelector(".h2 h2");
  header.innerHTML = `<strong>Reservation id:</strong> ${reservation.reservationId || "N/A"}`;
  
  const reservationInfo = document.querySelector(".reservation-info1");
  
  reservationInfo.innerHTML = `
      <p><strong>Room id:</strong> ${reservation.roomId || "N/A"}</p>
      <p><strong>Total cost:</strong> ${reservation.totalCost || "N/A"}</p>
      <p><strong>Start date:</strong> ${reservation.startDate || "N/A"}</p>
      <p><strong>End date:</strong> ${reservation.endDate || "N/A"}</p>
      <p><strong>Status:</strong> ${reservation.status || "N/A"}</p>
    `;

  const userInfo = document.querySelector(".user");
  userInfo.innerHTML = `
      <p><strong>Name:</strong> ${user.name || "N/A"}</p>
      <p><strong>Date of Birth:</strong> ${user.dateOfBirth || "N/A"}</p>
      <p><strong>Login:</strong> ${user.login || "N/A"}</p>
      <p><strong>Phone number:</strong> ${user.phone || "N/A"}</p>
      <p><strong>Document number:</strong> ${user.documentNumber || "N/A"}</p>
      <p><strong>Card number:</strong> ${user.cardNumber || "N/A"}</p>
    `;

}

populateFields(selectedData);
