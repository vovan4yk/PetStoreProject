package pet.store.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderData {
    private long id;
    private long petId;
    private int quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime shipDate;
    private String status;
    private boolean complete;
}
