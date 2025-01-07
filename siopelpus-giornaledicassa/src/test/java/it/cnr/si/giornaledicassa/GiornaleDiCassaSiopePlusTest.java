/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.si.giornaledicassa;

import it.cnr.si.siopeplus.exception.SIOPEPlusServiceNotInstantiated;
import it.cnr.si.siopeplus.giornaledicassa.FlussoGiornaleDiCassa;
import it.cnr.si.siopeplus.model.Lista;
import it.cnr.si.siopeplus.model.MessaggioXML;
import it.cnr.si.siopeplus.model.Risultato;
import it.cnr.si.siopeplus.service.GiornaleDiCassaSiopePlusFactory;
import it.cnr.si.siopeplus.service.GiornaleDiCassaSiopePlusService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@TestPropertySource(value = "classpath:application-test.properties")
public class GiornaleDiCassaSiopePlusTest {
    private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Value("${sign.username}")
    private String signUsername;
    @Value("${sign.password}")
    private String signPassword;
    @Value("${sign.otp}")
    private String signOTP;
    @Autowired
    private GiornaleDiCassaSiopePlusFactory giornaleDiCassaSiopePlusFactory;
    @Autowired
    private AbstractEnvironment environment;
    @Autowired
    private ApplicationContext applicationContext;

    static boolean validateAgainstXSD(InputStream xml, URL xsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsd);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Test
    public void parseFileGionrnaleDiCassa() throws SIOPEPlusServiceNotInstantiated, IOException {
       System.out.println("test");

        GiornaleDiCassaSiopePlusService giornaleDiCassaSiopePlusService = giornaleDiCassaSiopePlusFactory.getGiornaleDiCassaSiopePlusService("BT");

        Lista listaGiornaliDiCassa=giornaleDiCassaSiopePlusService.getListaMessaggi(
                LocalDateTime.of(2024,6,6,0,0,00,000),
                LocalDateTime.of(2024,6,7,23,59,59,000),
                false,
                null);
        List <Risultato> risultati =Optional.ofNullable(listaGiornaliDiCassa.getRisultati())
                .orElseGet(() -> Collections.emptyList())
                .stream()
                .collect(Collectors.toList());
        for( Risultato risultato:risultati){

        }

                        // byte[] bytes = Files.readAllBytes(Paths.get("C:/Users/salvio_ciro/Desktop/GDC-6 GiugnoBT/GDC_0058473_2024_2024-06-06_1719310809856.xml"));
       // final MessaggioXML<FlussoGiornaleDiCassa> messaggioXML= giornaleDiCassaSiopePlusService.parseMessageBytes(bytes,"giornaleDiCassaTest");
       // Assert.assertNotNull(messaggioXML);
    }
}
