
exports.registerForm = (req, resp) =>
    resp.render('registration_form', { title: 'Регистрация'});

exports.profileRedirect = function (req, resp) {
    return resp.redirect('/static/profile.html');
};



