package com.osalliance.keycloak;

import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.events.Details;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import javax.ws.rs.core.MultivaluedMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AgeValidator implements FormAction {

    static public String birthDateField = "birthDate";

    public void buildPage(FormContext context, LoginFormsProvider form) {

    }

    public void validate(ValidationContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        context.getEvent().detail(Details.REGISTER_METHOD, "form");

        if(context.getAuthenticatorConfig().getConfig().containsKey(AgeValidatorFactory.MIN_AGE)){
            Integer minAge = Integer.valueOf(context.getAuthenticatorConfig().getConfig().get(AgeValidatorFactory.MIN_AGE));

            List<FormMessage> errors = new ArrayList<FormMessage>();

            if(!formData.containsKey(birthDateField)){
                context.error("birth date missing");
                errors.add(new FormMessage("birthDate", "invalidBirthDateMessage"));
            }else {
                Date now = new Date();

                String birthDateStr = formData.getFirst(birthDateField);
                Date birthDate = null;
                try {
                    birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar birthCal = Calendar.getInstance(Locale.GERMANY);
                Calendar nowCal = Calendar.getInstance(Locale.GERMANY);
                birthCal.setTime(birthDate);
                nowCal.setTime(now);

                int yearsDiff = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
                if(birthCal.get(Calendar.MONTH) > nowCal.get(Calendar.MONTH) ||
                        (birthCal.get(Calendar.MONTH) == nowCal.get(Calendar.MONTH) &&
                                birthCal.get(Calendar.DATE) > nowCal.get(Calendar.DATE)) ){
                    yearsDiff--;
                }

                if (minAge > yearsDiff) {
                    context.error("you need to be at least "+minAge+" years old");
                    errors.add(new FormMessage(birthDateField, "invalidAgeMessage"));
                } else {
                    context.success();
                }
            }

            context.validationError(formData, errors);
            return;
        }

        context.success();

    }

    public void success(FormContext context) {
        UserModel user = context.getUser();
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if(formData.containsKey(birthDateField)){
            String birthDate = formData.getFirst(birthDateField);
            Calendar birthCal = Calendar.getInstance(Locale.GERMANY);
            try {
                birthCal.setTime( new SimpleDateFormat("yyyy-MM-dd").parse(birthDate));
                int year = birthCal.get(Calendar.YEAR);
                int month = birthCal.get(Calendar.MONTH)+1;//Java Calendar start at 0 = January

                String dateAttribute = String.valueOf(month)+"-"+String.valueOf(year);
                user.setAttribute(birthDateField, Collections.singletonList(dateAttribute));

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean requiresUser() {
        return false;
    }

    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return false;
    }

    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    public void close() {

    }
}
