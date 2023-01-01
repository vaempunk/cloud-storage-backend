getAllUrl = 'http://localhost:8080/modules/all'

const modulesTable = document.getElementById('modulesTable');

function displayModules(modulesJson) {
    for (const moduleJson of modulesJson) {

        const id = moduleJson['id'];
        const idCell = document.createElement('td');
        idCell.textContent = id;

        const name = moduleJson['name'];
        const nameCell = document.createElement('td');
        nameCell.textContent = name;

        const description = moduleJson['description'];
        const descriptionCell = document.createElement('td');
        descriptionCell.textContent = description;

        const dateCreated = moduleJson['dateCreated'];
        const dateCreatedCell = document.createElement('td');
        dateCreatedCell.textContent = dateCreated;

        const deleteButton = document.createElement('input');
        deleteButton.setAttribute('type', 'button');
        deleteButton.setAttribute('value', '🗑');
        deleteButton.onclick = deleteModule(id);
        const deleteButtonCell = document.createElement('td');
        deleteButtonCell.appendChild(deleteButton);

        const moduleRow = document.createElement('tr');
        moduleRow.appendChild(idCell);
        moduleRow.appendChild(nameCell);
        moduleRow.appendChild(descriptionCell);
        moduleRow.appendChild(dateCreatedCell);
        moduleRow.appendChild(deleteButtonCell);

        modulesTable.appendChild(moduleRow);

    }
}

fetch(getAllUrl)
    .then((response) => {
        if (!response.ok) {
            alert('НЕ ПОЛУЧИЛОСБ((');
        }
        return response.json();
    })
    .then((json) => {
        displayModules(json);
    })
    .catch((error) => alert('ERROR: ${error}'));

function deleteModule(moduleId) {

    return () => {
        const xhr = new XMLHttpRequest();
        const url = 'http://localhost:8080/modules/' + moduleId;

        xhr.open('DELETE', url, false);
        xhr.send();

        document.location.reload();
    }

}

function addModule() {
    const moduleName = document.getElementById('nameInput').value;
    const moduleDescription = document.getElementById('descriptionInput').value;
    const moduleJson = JSON.stringify({
        "name": moduleName,
        "description": moduleDescription
    });

    const xhr = new XMLHttpRequest();
    const url = 'http://localhost:8080/modules';

    xhr.open('POST', url, false);
    xhr.setRequestHeader('Content-Type', "application/json");

    xhr.send(moduleJson);

    document.location.reload();

}

const addButton = document.getElementById('addButton');
addButton.onclick = addModule;

function editModule() {
    const moduleId = document.getElementById('idEditInput').value;
    const moduleName = document.getElementById('nameEditInput').value;
    const moduleDescription = document.getElementById('descriptionEditInput').value;
    const moduleJson = JSON.stringify({
        "name": moduleName,
        "description": moduleDescription,
    });

    const xhr = new XMLHttpRequest();
    const url = 'http://localhost:8080/modules/' + moduleId;

    xhr.open('PUT', url, false);
    xhr.setRequestHeader('Content-Type', "application/json");

    xhr.send(moduleJson);

    document.location.reload();

}

const editButton = document.getElementById('editButton');
editButton.onclick = editModule;