package com.osalliance.keycloak;

import static org.mockito.Mockito.*;

import org.jboss.resteasy.spi.HttpRequest;
import org.junit.Test;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.events.Details;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.*;
import org.keycloak.services.resources.AttributeFormDataProcessor;
import org.keycloak.userprofile.UserProfileAttributes;
import org.keycloak.userprofile.profile.representations.AttributeUserProfile;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

public class AgeValidatorTest {

    @Test
    public void testInvalidAge(){

        AttributeUserProfile userProfile = Mockito.mock(AttributeUserProfile.class);
        UserProfileAttributes userProfileAttributes = Mockito.mock(UserProfileAttributes.class);
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        ValidationContext context = Mockito.mock(ValidationContext.class);
        AuthenticatorConfigModel authenticatorConfigModel = Mockito.mock(AuthenticatorConfigModel.class);

        MockedStatic<AttributeFormDataProcessor> mocked = mockStatic(AttributeFormDataProcessor.class);

        MultivaluedMap<String,String> formData = Mockito.mock(MultivaluedMap.class);

        EventBuilder eventBuilder= mock(EventBuilder.class);


        String minAge = "13";
        Map<String,String> config = new HashMap<>();
        config.put(AgeValidatorFactory.MIN_AGE,minAge);

        Calendar cal = Calendar.getInstance(Locale.GERMANY);
        cal.setTime(new Date());

        cal.add(Calendar.YEAR, -10);

        String ageString = cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DAY_OF_MONTH);

        when(formData.containsKey(AgeValidator.birthDateField)).thenReturn(true);
        when(formData.getFirst(AgeValidator.birthDateField)).thenReturn(ageString);
        when(httpRequest.getDecodedFormParameters()).thenReturn(formData);
        when(eventBuilder.detail(Details.REGISTER_METHOD, "form")).thenReturn(eventBuilder);
        when(context.getEvent()).thenReturn(eventBuilder);
        when(context.getHttpRequest()).thenReturn(httpRequest);
        when(userProfileAttributes.getFirstAttribute(AgeValidator.birthDateField)).thenReturn(minAge);
        when(userProfile.getAttributes()).thenReturn(userProfileAttributes);
        when(AttributeFormDataProcessor.toUserProfile(formData)).thenReturn(userProfile);
        when(context.getAuthenticatorConfig()).thenReturn(authenticatorConfigModel);
        when(authenticatorConfigModel.getConfig()).thenReturn(config);


        AgeValidator ageVerifier = new AgeValidator();
        ageVerifier.validate(context);

        verify(context,times(1)).error("you need to be at least "+minAge+" years old");
        mocked.close();

    }

    @Test
    public void testValidAge(){

        AttributeUserProfile userProfile = Mockito.mock(AttributeUserProfile.class);
        UserProfileAttributes userProfileAttributes = Mockito.mock(UserProfileAttributes.class);
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        ValidationContext context = Mockito.mock(ValidationContext.class);
        AuthenticatorConfigModel authenticatorConfigModel = Mockito.mock(AuthenticatorConfigModel.class);

        MockedStatic<AttributeFormDataProcessor> mocked = mockStatic(AttributeFormDataProcessor.class);

        MultivaluedMap<String,String> formData = Mockito.mock(MultivaluedMap.class);

        EventBuilder eventBuilder= mock(EventBuilder.class);

        String minAge = "13";
        Map<String,String> config = new HashMap<>();
        config.put(AgeValidatorFactory.MIN_AGE,minAge);

        Calendar cal = Calendar.getInstance(Locale.GERMANY);
        cal.setTime(new Date());

        cal.add(Calendar.YEAR, -15);

        String ageString = cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DAY_OF_MONTH);

        when(formData.containsKey(AgeValidator.birthDateField)).thenReturn(true);
        when(formData.getFirst(AgeValidator.birthDateField)).thenReturn(ageString);
        when(httpRequest.getDecodedFormParameters()).thenReturn(formData);
        when(eventBuilder.detail(Details.REGISTER_METHOD, "form")).thenReturn(eventBuilder);
        when(context.getEvent()).thenReturn(eventBuilder);
        when(context.getHttpRequest()).thenReturn(httpRequest);
        when(userProfile.getAttributes()).thenReturn(userProfileAttributes);
        when(AttributeFormDataProcessor.toUserProfile(formData)).thenReturn(userProfile);
        when(context.getAuthenticatorConfig()).thenReturn(authenticatorConfigModel);
        when(authenticatorConfigModel.getConfig()).thenReturn(config);


        AgeValidator ageVerifier = new AgeValidator();
        ageVerifier.validate(context);

        verify(context,times(1)).success();
        mocked.close();
    }

}
