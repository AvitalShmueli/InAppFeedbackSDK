# Example: Integrate Feedback in Your MainActivity
This example demonstrates how to initialize and trigger the feedback dialog from an `Activity`, such as `MainActivity`.

```java
public class MainActivity extends AppCompatActivity {
    private FeedbackFormManager feedbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Feedback Manager
        feedbackManager = FeedbackFormManager.getInstance(this);

        // Optional: Set a user ID (if your app supports user login)
        feedbackManager.setUserId("user_123");

        // Set up feedback trigger (e.g., button click)
        setupFeedbackButton();
    }

    private void setupFeedbackButton() {
        // Option 1: Trigger from a feedback button
        findViewById(R.id.feedback_button).setOnClickListener(v -> showFeedbackDialog());

        // Option 2: Trigger from options menu or gesture
        // Option 3: Trigger after a key event like purchase or level completion
    }

    private void showFeedbackDialog() {
        feedbackManager.getActiveFeedbackForm(
            this,
            getSupportFragmentManager(),
            new FeedbackFormManager.FeedbackFormCallback<FeedbackForm>() {
                @Override
                public void ready(FeedbackForm form) {
                    // Feedback dialog will be shown automatically
                    Log.d("FeedbackSDK", "Feedback form loaded: " + form.getTitle());
                }

                @Override
                public void failed(String errorMsg) {
                    Log.e("FeedbackSDK", "Failed to load feedback form: " + errorMsg);
                    Toast.makeText(MainActivity.this,
                        "Feedback is temporarily unavailable.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
        );
    }
}

```

### Notes:
- The feedback dialog is only shown if a form is active for your app's package name.
- The userId is optional but useful for associating feedback with users.
- You can trigger the dialog from any place in your app — not just a button.

---

# Example: Trigger Feedback from a Fragment
This example demonstrates how to initialize and trigger the feedback dialog from a `Fragment`, using `requireContext()` and `getParentFragmentManager()`.

```java
public class HomeFragment extends Fragment {

    private FeedbackFormManager feedbackManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Feedback Manager
        feedbackManager = FeedbackFormManager.getInstance(requireContext());

        // Optional: Set a user ID if your app supports login
        feedbackManager.setUserId("user_123");

        // Set up feedback trigger (e.g., button click)
        view.findViewById(R.id.feedback_button).setOnClickListener(v -> showFeedbackDialog());

        return view;
    }

    private void showFeedbackDialog() {
        feedbackManager.getActiveFeedbackForm(
            requireContext(),
            getParentFragmentManager(),
            new FeedbackFormManager.FeedbackFormCallback<FeedbackForm>() {
                @Override
                public void ready(FeedbackForm form) {
                    // Dialog is shown automatically
                    Log.d("FeedbackSDK", "Form loaded: " + form.getTitle());
                }

                @Override
                public void failed(String errorMsg) {
                    Log.e("FeedbackSDK", "Failed to load feedback form: " + errorMsg);
                    Toast.makeText(requireContext(),
                        "Feedback is temporarily unavailable.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
        );
    }
}
```

### Notes:
- `requireContext()` and `getParentFragmentManager()` are used instead of `this` and `getSupportFragmentManager()` (which are for Activities).
- You can trigger the feedback from any UI event inside the fragment.
- The dialog will only appear if an active form exists for your app's package.



[View more integration patterns](./example-integration-patterns)