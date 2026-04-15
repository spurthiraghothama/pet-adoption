@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(taskService.createTask(payload));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Task> acceptTask(@PathVariable Long id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }
}