package validation;

import helpers.UserHelper;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class UsernameConstraintValidator implements ConstraintValidator<Username, String> {

    private static final UserHelper USERHELPER = new UserHelper();
    
    @Override
    public void initialize(Username a) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null)
            return false;

        return USERHELPER.getUser(value) == null;
    }
}
