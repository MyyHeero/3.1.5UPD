let userToDelete = null;
let currentUser = null; // Хранение текущего пользователя для панели пользователя

$(document).ready(function() {
    loadRoles();
    loadCurrentUserInfo();
});

function confirmLogout() {
    if (confirm("Are you sure you want to log out?")) {
        window.location.href = "/logout";
    }
}

// Загружаем информацию о текущем пользователе и настраиваем доступ к панелям
function loadCurrentUserInfo() {
    $.ajax({
        url: '/api/user',
        method: 'GET',
        success: function(user) {
            currentUser = user; // Сохраняем текущего пользователя
            $('#currentUserInfo').text(`${user.username} with roles: ${user.roles.map(role => role.replace('ROLE_', '')).join(' ')}`);

            // Проверяем роль и отображаем соответствующую панель
            if (user.roles.includes('ROLE_ADMIN')) {
                showAdminPanel();
                showUsersTable();
            } else {
                showUserPanel();
                $('#adminButton').hide(); // Скрываем кнопку "Admin" для обычного пользователя
            }
        },
        error: function(err) {
            console.error('Ошибка при загрузке данных текущего пользователя:', err);
        }
    });
}

// Отображаем панель администратора
function showAdminPanel() {
    $('#adminPanel').show();
    $('#userPanel').hide();
    $('#addUserSection').hide(); // Скрываем секцию добавления нового пользователя
    $('#adminButton').addClass('active');
    $('#userButton').removeClass('active');
}

// Отображаем панель пользователя с информацией о текущем пользователе
function showUserPanel() {
    $('#adminPanel').hide();
    $('#addUserSection').hide(); // Скрываем секцию добавления нового пользователя
    $('#userPanel').show();      // Показываем панель пользователя
    $('#adminButton').removeClass('active');
    $('#userButton').addClass('active');

    if (currentUser) {
        loadUserInfo(currentUser);
    }
}

// Загрузка информации о текущем пользователе в панель пользователя
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

// Загружаем роли в выпадающий список
function loadRoles() {
    $.ajax({
        url: '/api/roles',
        method: 'GET',
        success: function(roles) {
            $('#editRoles').empty();
            $('#editRolesModal').empty();
            roles.forEach(role => {
                $('#editRoles').append(`<option value="${role.name}">${role.name.replace('ROLE_', '')}</option>`);
                $('#editRolesModal').append(`<option value="${role.name}">${role.name.replace('ROLE_', '')}</option>`);
            });
        },
        error: function(err) {
            console.error('Ошибка при загрузке ролей:', err);
        }
    });
}

// Показываем таблицу пользователей
function showUsersTable() {
    $('#userTableContainer').show();
    $('#addUserSection').hide();
    fetchUsers();
}

// Показываем секцию для добавления нового пользователя
function showAddUserForm() {
    $('#userTableContainer').hide();
    $('#addUserSection').show();
    $('#editUserId').val('');
    $('#editFirstName').val('');
    $('#editLastName').val('');
    $('#editAge').val('');
    $('#editUsername').val('');
    $('#editPassword').val('');
    $('#editRoles').val([]);
    loadRoles();
}

// Загружаем всех пользователей для таблицы
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

// Сохранение нового пользователя
function saveUser() {
    const user = {
        firstName: $('#editFirstName').val(),
        lastName: $('#editLastName').val(),
        age: $('#editAge').val(),
        username: $('#editUsername').val(),
        password: $('#editPassword').val() || null,
        roles: $('#editRoles').val()
    };

    $.ajax({
        url: '/api/admin/users',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function() {
            showUsersTable();
        },
        error: function(err) {
            console.error('Ошибка при сохранении пользователя:', err);
        }
    });
}

// Открываем модальное окно для редактирования пользователя
function editUser(id) {
    $.ajax({
        url: `/api/admin/users/${id}`,
        method: 'GET',
        success: function(user) {
            $('#editUserIdModal').val(user.id);
            $('#editFirstNameModal').val(user.firstName);
            $('#editLastNameModal').val(user.lastName);
            $('#editAgeModal').val(user.age);
            $('#editUsernameModal').val(user.username);
            $('#editPasswordModal').val('');
            $('#editRolesModal').val(user.roles.map(role => role.name));
            $('#editUserModal').modal('show'); // Показываем модальное окно для редактирования
        },
        error: function(err) {
            console.error('Ошибка при загрузке данных пользователя:', err);
        }
    });
}

// Сохранение отредактированного пользователя
function saveEditedUser() {
    const user = {
        id: $('#editUserIdModal').val(),
        firstName: $('#editFirstNameModal').val(),
        lastName: $('#editLastNameModal').val(),
        age: $('#editAgeModal').val(),
        username: $('#editUsernameModal').val(),
        password: $('#editPasswordModal').val() || null,
        roles: $('#editRolesModal').val()
    };

    $.ajax({
        url: `/api/admin/users`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function() {
            $('#editUserModal').modal('hide');
            fetchUsers();
        },
        error: function(err) {
            console.error('Ошибка при обновлении пользователя:', err);
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
