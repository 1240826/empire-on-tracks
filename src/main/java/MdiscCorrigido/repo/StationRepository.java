package MdiscCorrigido.repo;

import MdiscCorrigido.domain.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Repositório responsável por carregar estações de um ficheiro CSV.
 *
 * Cada token no CSV deve ter o formato:
 *   <Tipo><Nome>
 * onde:
 *   – <Tipo> é exatamente o primeiro carácter (’D’, ’S’ ou ’T’)
 *   – <Nome> é o identificador completo (podendo conter dígitos, letras, etc.)
 *
 * Exemplos de tokens válidos: "D8", "S2", "T10", "D12", "S_StationX" (se quiser usar underscore dentro do nome).
 */
public class StationRepository {
    private final String stationsFile;

    public StationRepository(String stationsFile) {
        this.stationsFile = Objects.requireNonNull(stationsFile, "stationsFile must not be null");
    }

    /**
     * Lê a única linha do ficheiro de estações, separa em tokens por ';' e, para cada token:
     *  1) Verifica se o primeiro carácter é 'D', 'S' ou 'T'
     *  2) Pega o token inteiro (por exemplo "D8", "S2", "T10") como nome da estação
     *  3) Cria new Station(token), que internamente infere StationType.fromName(token)
     *
     * @return lista ordenada de Station
     * @throws FileNotFoundException se não encontrar o ficheiro
     */
    public List<Station> loadAll() throws FileNotFoundException {
        List<Station> stations = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(stationsFile))) {
            if (!sc.hasNextLine()) {
                return stations;
            }
            String line = sc.nextLine();
            for (String token : line.split(";")) {
                token = token.trim();
                if (token.isEmpty()) {
                    continue;
                }
                char prefix = Character.toUpperCase(token.charAt(0));
                if (prefix != 'D' && prefix != 'S' && prefix != 'T') {
                    // Ignora tokens cujo primeiro carácter não seja D, S ou T
                    continue;
                }
                // Usa o token inteiro como nome. Ex.: "D8", "S2", "T10".
                Station s = new Station(token);
                stations.add(s);
            }
        }
        // Ordena primeiro por tipo (DETETADO em StationType.fromName), depois por nome lexicográfico
        stations.sort(Comparator
                .comparing(Station::getType)
                .thenComparing(Station::getName));
        return stations;
    }
}
