import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Appbar from 'muicss/lib/react/appbar';
import HomePage from "./HomePage";
import RegistrationPage from "./RegistrationPage";
import UsersPage from "./UsersPage";
import CatSearchPage from "./CatSearchPage";

class NavigationBar extends React.Component{

    render() {
        let s1 = {verticalAlign: 'middle'};
        let s2 =
            {
                textAlign: 'right',
                marginRight: 5
            };

        return (
            <Appbar>
                <Router>
                    <table width="100%">
                        <tbody>
                        <tr style={s1}>
                            <td className="mui--appbar-height">AsyncApp</td>
                            <Link to="/">
                                <td className="mui--appbar-height" style={s2}>Стартовая страница</td>
                            </Link>

                            <Link to="/cat_receive">
                                <td className="mui--appbar-height" style={s2}>Получение кота</td>
                            </Link>

                            <Link to="/register">
                                <td className="mui--appbar-height" style={s2}>Регистрация</td>
                            </Link>

                            <Link to= "/users">
                                <td className="mui--appbar-height" style={s2}>Список пользователей</td>
                            </Link>
                        </tr>
                        </tbody>
                    </table>

                    <Route exact path="/" component={HomePage} />
                    <Route path="/register" component={RegistrationPage} />
                    <Route path="/cat_receive" component={CatSearchPage} />
                    <Route path="/users" component={UsersPage} />

                </Router>

            </Appbar>
        );
    }

}
export default NavigationBar;