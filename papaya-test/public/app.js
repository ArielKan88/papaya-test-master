// Get the list of departments when loading
$(document).ready(function () {
    $.get('/departments')
        .done(function (data) {
            var departments = JSON.parse(data);
            departments.forEach(function (department) {
                addDepartmentItem(department);
            });           
        });
});

// Add a department
function addDepartment() {
    var newDepartmentName = $('#new-department').val();
    $.post('/department?name=' + newDepartmentName)
        .done(function (data) {
            addDepartmentItem(JSON.parse(data));
        });
}

// Delete a department
function deleteDepartment(departmentId) {
    $.ajax({
        url: '/department/' + departmentId,
        type: 'DELETE'
    })
        .done(function () {
            $('#department-' + departmentId).remove();
        });
}

// Create a debounced version of update
var debouncedUpdate = debounce(250, updateDepartment);
function onDepartmentChange(departmentId, departmentName) {    
    // Handle department change
    debouncedUpdate(departmentId, departmentName);
}

// Update a department
function updateDepartment(departmentId, departmentName) {
    $.post('/department/' + departmentId + '?name=' + departmentName);
}

// Add a department to the departments list
function addDepartmentItem(department) {
    var departmentItem = '<li id="department-' + department.id + '">Id: ' + department.id + '&nbspName: ' + 
        '<input type="text" value="' + department.name + '">'+
        '&nbsp<button>delete</button>' +
        '</li>';
    $('#departments').append(departmentItem);
    // Handle onClick for the delete button
    $('#department-' + department.id + " button").click(deleteDepartment.bind(null, department.id));
    // Handle department name change
    $('#department-' + department.id + " input").keyup(function() {
        onDepartmentChange(department.id, this.value)
    });
}

// Debounce a function
function debounce(wait, func) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            func.apply(context, args);
        };       
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
};