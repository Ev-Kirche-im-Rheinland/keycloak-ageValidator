package com.osalliance.keycloak;

import org.keycloak.Config;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Collections;
import java.util.List;

import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

public class AgeValidatorFactory implements FormActionFactory {
    public static final String PROVIDER_ID = "agecheck-block-action";
    protected static final String MIN_AGE = "years";

    public static final AgeValidator authenticator = new AgeValidator();

    public String getDisplayType() {
        return "Age Validation";
    }

    public String getReferenceCategory() {
        return null;
    }

    public boolean isConfigurable() {
        return true;
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };

    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    public boolean isUserSetupAllowed() {
        return false;
    }

    public String getHelpText() {
        return null;
    }

    public List<ProviderConfigProperty> getConfigProperties() {

        ProviderConfigProperty rep = new ProviderConfigProperty();
        rep.setHelpText( "Minimum Age in years");
        rep.setName(MIN_AGE);
        rep.setLabel("years");
        rep.setType(STRING_TYPE);

        return Collections.singletonList(rep);
    }

    public FormAction create(KeycloakSession session) {
        return authenticator;
    }

    public void init(Config.Scope config) {

    }

    public void postInit(KeycloakSessionFactory factory) {

    }

    public void close() {

    }

    public String getId() {
        return PROVIDER_ID;
    }
}
