package kr.hoppang.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import kr.hoppang.domain.user.User;

public class UserSerializerForUserCache extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("id", user.getId().toString());
        gen.writeStringField("name", user.getName());
        gen.writeStringField("email", user.getEmail());
        gen.writeStringField("tel", user.getTel());
        gen.writeStringField("userRole", user.getUserRole().name());

        gen.writeEndObject();
    }
}