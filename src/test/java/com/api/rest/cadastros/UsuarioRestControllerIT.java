package com.api.rest.cadastros;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.dto.ModuloDTO;
import com.api.dto.PermissaoDTO;
import com.api.dto.RespostaDTO;
import com.api.dto.UsuarioDTO;
import com.api.service.cadastros.usuario.UsuarioService;
import com.api.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author juliano.ezequiel
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioRestControllerIT {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsuarioService usuarioService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void ListarTodos() throws Exception {

        ResultActions result = this.mvc.perform(get("/teste/usuario").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8_VALUE));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        String json = result.andReturn().getResponse().getContentAsString();

        List<UsuarioDTO> list = mapper.readValue(json, new TypeReference<List<UsuarioDTO>>() {
        });

        System.out.println(json);

        list.stream().map(u -> u.getId()).forEach(f -> buscar(f));

        Assert.assertTrue(true);
    }

    public void buscar(Integer id) {
        try {
            ResultActions result = this.mvc.perform(
                    get("/teste/usuario/" + id.toString())
                    .param("id", id.toString())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE));

            result.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
            String json = result.andReturn().getResponse().getContentAsString();

            UsuarioDTO usuarioDTO = mapper.readValue(json, UsuarioDTO.class);

            System.out.println(json);

            inserir();

            Assert.assertTrue(true);
        } catch (Exception ex) {
        }
    }

    public void inserir() {
        try {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setAdmin(Boolean.FALSE);
            usuarioDTO.setEmail("teste@teste.com");
            
            List<PermissaoDTO> list = new ArrayList<>();
            PermissaoDTO permissaoDTO = new PermissaoDTO();
            permissaoDTO.setDescricao("permissao teste");
            
            list.add(permissaoDTO);
            permissaoDTO.setModuloDTO(new ModuloDTO());
            usuarioDTO.setPermissoes(list);

            ResultActions result = this.mvc.perform(
                    MockMvcRequestBuilders.post("/teste/usuario")
                    .content(Utils.asJsonString(usuarioDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE));

            result.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

            String json = result.andReturn().getResponse().getContentAsString();

            RespostaDTO respostaDTO = mapper.readValue(json, RespostaDTO.class);

            Assert.assertTrue(true);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioRestControllerIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
