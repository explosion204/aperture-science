import PropTypes from 'prop-types';

const Button = ({text, color, btnClass, onClick, btnStyles}) => {
    return (
        <button className={"btn m-1 " + btnClass} 
                style={{ backgroundColor: color, color: "white", ...btnStyles }}
                onClick={onClick}>{text}</button>
    )
};

Button.propTypes = {
    text: PropTypes.string,
    color: PropTypes.string,
    btnClass: PropTypes.string,
    onClick: PropTypes.func,
    btnStyles: PropTypes.object
};

export default Button;