package validation;

import java.util.HashMap;
import java.util.Map;
import javax.validation.metadata.*;
import org.primefaces.validate.bean.ClientValidationConstraint;

class PasswordClientValidationConstraint implements ClientValidationConstraint {
    public static final String MESSAGE_METADATA = "data-lozinka-chars";
    
    @Override
    public Map<String, Object> getMetadata(ConstraintDescriptor cd) {
        Map<String,Object> metadata = new HashMap<>();
        Map attrs = cd.getAttributes();
        Object message = attrs.get("message");    
        if(message != null) {
            metadata.put(MESSAGE_METADATA, message);
        }
        return metadata;
    }

    @Override
    public String getValidatorId() {
        return Password.class.getSimpleName();
    }
    
}
