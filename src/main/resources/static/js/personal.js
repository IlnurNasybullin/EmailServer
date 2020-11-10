const requestURL = 'http://localhost:9001/email/getUser';

function sendRequest(method, url, body = null) {
    return fetch(url).then(response => {
        return response.json();
    })
}

var nameInput = document.getElementById("name");
var passwordInput = document.getElementById("password");

sendRequest('GET', requestURL).then(data => {
    nameInput.value = data["name"];
    passwordInput.value = data["password"]
}).catch(err => console.log(err))

var changeButton = document.getElementById("changeButton");
var saveButton = document.getElementById('saveButton');

var onChange = false;

changeButton.onclick = function() {
    if (!onChange) {
        nameInput.readOnly = false;
        passwordInput.readOnly = false;

        saveButton.disabled = false;
        onChange = true;
    }
}