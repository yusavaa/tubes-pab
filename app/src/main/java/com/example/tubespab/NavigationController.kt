import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button
import com.example.tubespab.InventoryActivity
import com.example.tubespab.MainActivity
import com.example.tubespab.R

object NavigationController {

    private fun handleNavigation(context: Context, buttonId: String) {
        val intent = when (buttonId) {
            "home" -> Intent(context, MainActivity::class.java)
            "inventory" -> Intent(context, InventoryActivity::class.java)
            else -> null
        }

        intent?.let {
            context.startActivity(it)
        }
    }

    fun navAction(activity: Activity) {
        val buttonHome: Button = activity.findViewById(R.id.homeButton)
        val buttonInventory: Button = activity.findViewById(R.id.inventoryButton)

        buttonHome.setOnClickListener {
            handleNavigation(activity, "home")
        }

        buttonInventory.setOnClickListener {
            handleNavigation(activity, "inventory")
        }
    }
}
