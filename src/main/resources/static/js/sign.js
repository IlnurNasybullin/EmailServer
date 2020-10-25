var submitForm = document.querySelector('.form')
var email = document.querySelector('.login')
var passwordForm = document.querySelector('.password');

var without_trims = function(e) {
	if (e.keyCode == 32) {
		e.preventDefault();
	}
}

email.addEventListener('keydown', without_trims);
passwordForm.addEventListener('keydown', without_trims);

submitForm.addEventListener("submit", function(e) {
	if (isEmpty(email.value)) {
		console.log("uncorrect")
	}

	console.log("submit")
}, false);

function isEmpty(str) {
	return str.trim() === '';
}