package rifki.bicycle_rent.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TopUpDTO {
    @Positive(message = "Jumlah saldo harus lebih besar dari nol")
    private Long topUpBalance;
}
