@RestController
@RequestMapping("api/v1/formulaire")
@Tag(name="formulaires", description = "Gestion des rendus de formulaires")
@RequiredArgsConstructor
public class FormulaireController {
    private final FormulaireService formulaireService;

    @GetMapping("/afficher/{rendu}")
    @Operation(summary = "Affiche un formulaire selon le rendu choisi (html ou widget)")
    public ResponseEntity<String> afficherFormulaire(@PathVariable String rendu) {
        return ResponseEntity.ok(formulaireService.genererFormulaire(rendu));
    }
}