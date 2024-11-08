let userToDelete = null;

$(document).ready(function() {
    loadRoles();
    loadCurrentUserInfo();
});

function confirmLogout() {
    if (confirm("Are you sure you want to log out?")) {
        window.location.href = "/logout";
    }
}

function loadCurrentUserInfo() {
    $.ajax({
        url: '/api/user',
        method: 'GET',
        success: function(user) {
            $('#currentUserInfo').text(`${user.username} with roles: ${user.roles.map(role => role.replace('ROLE_', '')).join(' ')}`);
            loadUserInfo(user);

            if (user.roles.includes('ROLE_ADMIN')) {
                showAdminPanel();
                showUsersTable();
            } else {
                showUserPanel();
                $('#adminButton').hide();
            }
        },
        error: function(err) {
            console.error('Ошибка при загрузке данных текущего пользователя:', err);
        }
    });
}

function showAdminPanel() {
    $('#adminPanel').show();
    $('#userPanel').hide();
    $('#adminButton').addClass('active');
    $('#userButton').removeClass('active');
}

function showUserPanel() {
    $('#adminPanel').hide();
    $('#userPanel').show();
    $('#adminButton').removeClass('active');
    $('#userButton').addClass('active');
}

function loadUserInfo(user) {
    $('#userInfo').html(`
        <tr>
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.username}</td>
            <td>${user.roles.map(role => role.replace('ROLE_', '')).join(' ')}</td>
        </tr>
    `);
}

function loadRoles() {
    $.ajax({
        url: '/api/roles',
        method: 'GET',
        success: function(roles) {
            $('#editRoles').empty();
            roles.forEach(role => {
                $('#editRoles').append(`<option value="${role.name}">${role.name.replace('ROLE_', '')}</option>`);
            });
        },
        error: function(err) {
            console.error('Ошибка при загрузке ролей:', err);
        }
    });
}

function showUsersTable() {
    $('#userTableContainer').show();
    fetchUsers();
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
    const userId = $('#editUserId').val();
    const user = {
        id: userId,
        firstName: $('#editFirstName').val(),
        lastName: $('#editLastName').val(),
        age: $('#editAge').val(),
        username: $('#editUsername').val(),
        password: $('#editPassword').val() || null,  // Оставляем пароль null, если поле пустое
        roles: $('#editRoles').val()
    };

    const url = userId ? '/api/admin/users' : '/api/admin/users';
    const method = userId ? 'PUT' : 'POST';

    $.ajax({
        url: url,
        method: method,
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function() {
            $('#editUserModal').modal('hide');
            fetchUsers();
        },
        error: function(err) {
            console.error('Ошибка при сохранении пользователя:', err);
        }
    });
}

function editUser(id) {
    $.ajax({
        url: `/api/admin/users/${id}`,
        method: 'GET',
        success: function(user) {
            $('#editUserId').val(user.id);
            $('#editFirstName').val(user.firstName);
            $('#editLastName').val(user.lastName);
            $('#editAge').val(user.age);
            $('#editUsername').val(user.username);
            $('#editPassword').val('');  // Очищаем поле, чтобы при редактировании оно отображалось как пустое
            $('#editRoles').val(user.roles.map(role => role.name));
            $('#editUserModal').modal('show');
        },
        error: function(err) {
            console.error('Ошибка при загрузке данных пользователя:', err);
        }
    });
}

function confirmDeleteUser(id) {
    $.ajax({
        url: `/api/admin/users/${id}`,
        method: 'GET',
        success: function(user) {
            $('#modalFirstName').text(user.firstName);
            $('#modalLastName').text(user.lastName);
            $('#modalAge').text(user.age);
            $('#modalEmail').text(user.username);
            $('#modalRoles').text(user.roles.map(role => role.name.replace('ROLE_', '')).join(', '));
            userToDelete = id;
            $('#deleteUserModal').modal('show');
        },
        error: function(err) {
            console.error('Ошибка при загрузке данных пользователя для удаления:', err);
        }
    });
}

$('#confirmDeleteBtn').click(function() {
    if (userToDelete) {
        $.ajax({
            url: `/api/admin/users/${userToDelete}`,
            method: 'DELETE',
            success: function() {
                $('#deleteUserModal').modal('hide');
                fetchUsers();
                userToDelete = null;
            },
            error: function(err) {
                console.error('Ошибка при удалении пользователя:', err);
            }
        });
    }
});
