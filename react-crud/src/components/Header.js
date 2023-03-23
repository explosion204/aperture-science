import PropTypes from 'prop-types';
import Button from './Button'; 
import { useLocation, Link } from 'react-router-dom';

const Header = ({ text, formIsVisible, onButtonClick }) => {
    const location = useLocation();

    return (
        <header className="d-flex align-items-center w-100 h-25 shadow-sm p-2 mb-4">
            <h1>{text}</h1>
            <hr/>
            { 
                location.pathname === '/' &&
                <Button text={formIsVisible ? "Close" : "Add"}
                        color={formIsVisible ? "red" : "green"}
                        btnClass="ms-auto"
                        onClick={onButtonClick}/>
            }
            {
                location.pathname === '/' ? <Link to='/about'>
                    <Button text='About'
                            color='green'/>
                    </Link> : <Link to='/' className='ms-auto'>
                        <Button text='Go back' color='green' btnClass='ms-auto'/>
                    </Link>
            }

        </header>
    )
};

Header.propTypes = {
    text: PropTypes.string,
    formIsVisible: PropTypes.bool,
    onButtonClick: PropTypes.func
};

export default Header;
