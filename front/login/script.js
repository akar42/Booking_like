const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});


const adminData = [
    {
      login: "admin2@gmail.com",
      adminId: "admin2",
      password: "1234", 
      role: "admin",
    },
    {
      login: "admin1@gmail.com",
      adminId: "admin1",
      password: "1212",
      role: "admin",
    },
  ];

  const userData = [
    {
        login: "user1@gmail.com",
        userId: 12345,
        password: "user1",
        role: "user"
    },
    {
        login: "user2@gmail.com",
        userId: 123,
        password: "user2",
        role: "user"
    }
  ]

  function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = `expires=${date.toUTCString()}`;
    document.cookie = `${name}=${encodeURIComponent(value)}; ${expires}; path=/`;
  }

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

  function handleLogin(event) {
    event.preventDefault();
    const emailInput = document.querySelector(".sign-in input[type='email']").value;
    const passwordInput = document.querySelector(".sign-in input[type='password']").value;
  
    console.log("Email:", emailInput);
    console.log("Password:", passwordInput);      
  
    const isAdmin = adminData.find(
        (admin) => admin.login === emailInput && admin.password === passwordInput
      );
  
      if (isAdmin && isAdmin.role === "admin") {
        setCookie("isLoggedIn", true, 1);
        setCookie("adminRole", isAdmin.role, 1); 
        setCookie("adminId", isAdmin.adminId, 1);
        window.location.href = "admin_active_reservations.html";
        return;
      } 


    const isUser = userData.find(
       (user) => user.login === emailInput && user.password === passwordInput
    );

      if(isUser && isUser.role === "user"){
        setCookie("isLoggedIn", true, 1);
        setCookie("userRole", isUser.role, 1);
        setCookie("userId", isUser.userId, 1);
        window.location.href = "my_profile.html";
        return;
      }
  }
  
  document.querySelector(".sign-in form").addEventListener("submit", handleLogin);