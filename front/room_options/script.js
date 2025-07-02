const editBtn = document.getElementById('editRoomBtn');
const addBtn = document.getElementById('addRoomBtn');
const deleteBtn = document.getElementById('deleteRoomBtn');
const formSection = document.getElementById('formSection');
const closeFormBtn = document.getElementById('closeForm');
const roomForm = document.getElementById('roomForm');

function showForm() {
  formSection.classList.remove('hidden');
}


function hideForm() {
  formSection.classList.add('hidden');
}


editBtn.addEventListener('click', function() {

  showForm();
});

addBtn.addEventListener('click', function() {

  roomForm.reset();
  showForm();
});


deleteBtn.addEventListener('click', function() {
  
  if (confirm('Are you sure you want to delete this room?')) {
    alert('Room was deleted.');
    
  } else {
    alert('You did not delete this room.');
  }
});

closeFormBtn.addEventListener('click', hideForm);

roomForm.addEventListener('submit', function(event) {
  event.preventDefault();
  alert('Submited.');
  hideForm();
});
