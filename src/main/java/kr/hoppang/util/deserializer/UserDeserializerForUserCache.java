package kr.hoppang.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;

public class UserDeserializerForUserCache extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        return User.of(
                node.get("id").asLong(),
                node.get("name").asText(),
                node.get("email").asText(),
                null,
                node.get("tel").asText(),
                UserRole.from(node.get("userRole").asText()),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}