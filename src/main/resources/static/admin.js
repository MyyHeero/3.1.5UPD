let userToDelete = null;

$(document).ready(function() {
    loadRoles();
    loadAdminView();
});

function loadRoles() {
    $.ajax({
        url: '/api/roles',
        method: 'GET',
        success: function(roles) {
            $('#roles').empty();
            roles.forEach(role => {
                $('#roles').append(`<option value="${role.name}">${role.name.replace('ROLE_', '')}</option>`);
            });
        },
        error: function(err) {
            console.error('Ошибка при загрузке ролей:', err);
        }
    });
}

function loadAdminView() {
    $('#adminPanel').show();
    $('#userFormContainer').hide();
    showUsersTable();
}

function showUsersTable() {
    $('#userTableContainer').show();
    $('#userFormContainer').hide();
    $('#statusBar').text('All Users');
    fetchUsers();
}

function showUserForm() {
    $('#userTableContainer').hide();
    $('#userFormContainer').show();
    $('#statusBar').text('Add New User');
    clearForm();
}

function clearForm() {
    $('#userId').val('');
    $('#firstName').val('');
    $('#lastName').val('');
    $('#age').val('');
    $('#username').val('');
    $('#password').val('');
    $('#roles').val('');
}

function fetchUsers() {
    $.ajax({
        url: '/api/admin/users',
        method: 'GET',
        success: function(users) {
            let tableContent = `<table class="table table-striped table-hover">
                <thead><tr><th>ID</th><th>First Name</th><th>Last Name</th><th>Age</th><th>Email</th><th>Roles</th><th>Actions</th></tr></thead>
                <tbody>`;
            users.forEach(user => {
                tableContent += `<tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.username}</td>
                    <td>${user.roles.map(role => role.name.replace('ROLE_', '')).join(', ')}</td>
                    <td class="table-actions">
                        <button onclick="editUser(${user.id})" class="btn btn-warning btn-sm">
                            <i class="fas fa-edit"></i> Edit
                        </button>
                        <button onclick="confirmDeleteUser(${user.id})" class="btn btn-danger btn-sm">
                            <i class="fas fa-trash-alt"></i> Delete
                        </button>
                    </td>
                </tr>`;
            });
            tableContent += `</tbody></table>`;
            $('#userTableContainer').html(tableContent);
        },
        error: function(err) {
            console.error('Ошибка при загрузке пользователей:', err);
        }
    });
}

function saveUser() {
    const userId = $('#userId').val();
    const user = {
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        age: $('#age').val(),
        username: $('#username').val(),
        password: $('#password').val(),
        roles: $('#roles').val()
    };

    const url = userId ? `/api/admin/users/${userId}` : '/api/admin/users';
    const method = userId ? 'PUT' : 'POST';

    $.ajax({
        url: url,
        method: method,
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function() {
            showUsersTable();
            clearForm();
            $('#userFormContainer').hide();
            $('#userTableContainer').show();
            $('#statusBar').text('All Users');
        },
        error: function(err) {
            console.error('Ошибка при сохранении пользователя:', err);
        }
    });
}

function confirmDeleteUser(id) {
    userToDelete = id;

    $.ajax({
        url: `/api/admin/users/${id}`,
        method: 'GET',
        success: function(user) {
            $('#modalFirstName').text(user.firstName);
            $('#modalLastName').text(user.lastName);
            $('#modalAge').text(user.age);
            $('#modalUsername').text(user.username);
            $('#modalRoles').text(user.roles.map(role => role.name.replace('ROLE_', '')).join(', '));
            $('#deleteUserModal').modal('show');
        },
        error: function(err) {
            console.error('Ошибка при загрузке данных пользователя:', err);
        }
    });
}

// Удаление пользователя при подтверждении в модальном окне
$('#confirmDeleteBtn').click(function() {
    if (userToDelete) {
        $.ajax({
            url: `/api/admin/users/${userToDelete}`,
            method: 'DELETE',
            success: function() {
                $('#deleteUserModal').modal('hide'); // Закрываем модальное окно
                fetchUsers(); // Обновляем список пользователей после удаления
                userToDelete = null; // Сбрасываем переменную после удаления
            },
            error: function(err) {
                console.error('Ошибка при удалении пользователя:', err);
            }
        });
    }
});
